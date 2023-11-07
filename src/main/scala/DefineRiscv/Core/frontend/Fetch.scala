 /***************************************************************************************
MIT License

Copyright (c) 2023 xxl

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 ***************************************************************************************/

package DefineRiscv.Core.frontend

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core.{U, _}
import spinal.lib._
import DefineRiscv.Core.{coreParameters, frontend}
import spinal.lib.bus.amba4.axi._
import spinal.core.sim._
/* Fetch stage can convert the fetch cmd to the ITCM or DRAM */

case class FetchCmd(p:coreParameters) extends Bundle{
  val pc = UInt(p.Xlen bits)
  val io = Bool()  /* whether is Io request */
}

case class FetchRsp(p:coreParameters) extends Bundle{
  val pc = UInt(p.Xlen bits)
  val instruction = Bits(p.instructionWidth bits)
  /* branch rsp */
}

case class FetchBus(p:coreParameters) extends Bundle with IMasterSlave {
  val fetchCmd = Stream(FetchCmd(p))
  val fetchRsp = Flow(FetchRsp(p))
  override def asMaster(): Unit = {
    master(fetchCmd)
    slave(fetchRsp)
  }

  /*convert it to the axi4 readonly bus */
  def toAxi4ReadOnly(): Axi4ReadOnly = {
    val bus = Axi4ReadOnly(p.SimpleMemoryibusConfig)
    val pcValue = RegNextWhen(fetchCmd.pc, bus.ar.fire)

    bus.ar.valid := fetchCmd.valid
    bus.ar.addr := fetchCmd.pc
    fetchCmd.ready := bus.ar.ready
    bus.ar.size := U(2).resized
    bus.ar.len := U(0).resized
    bus.r.ready := True

    fetchRsp.instruction := bus.r.payload.data
    fetchRsp.pc := pcValue
    fetchRsp.valid := bus.r.valid
    bus
  }
}

case class FetchOut(p:coreParameters) extends Bundle {
  val pc = UInt(p.Xlen bits)
  val instruction = Bits(p.instructionWidth bits)
}


class Fetch(p:coreParameters) extends PrefixComponent{
  import p._
  val io = new Bundle{
    val halt = in Bool()
    val flush = in Bool()
    val pcLoad = slave Flow(UInt(p.Xlen bits)) /* branch jump or exception jump*/
    val fetchOut = master Stream (FetchOut(p))
    val fetchBus = master (FetchBus(p)) /* trans the no-IO request to the cache or simple memory */
  }
  val fetchRequest = FetchBus(p)

  val preFetch = new Area{
    val pc = Reg(UInt(Xlen bits)).init(resetValue)
    val pcNext = if(withRVC) pc + 2 else pc + 4
    val inc = False
    val reset = RegInit(True)
    reset := False
    pc := Mux(inc && !reset,pcNext,pc) /* if receive increase then pc plus */

    val fetchValid = !reset && !io.halt

    when(io.pcLoad.valid) {
        pc := io.pcLoad.payload
    }
  }

  val Fetch = new Area {

    when(fetchRequest.fetchCmd.fire && !io.pcLoad.valid ){
      preFetch.inc := True
    }
    /* check if the io request OR dram request */
    val itcm = new TCM(p,p.itcmParameters)

    itcm.io.request.fetchCmd.valid := False
    itcm.io.request.fetchCmd.io := False
    itcm.io.request.fetchCmd.payload.pc := preFetch.pc
    io.fetchBus.fetchCmd.valid := False
    io.fetchBus.fetchCmd.io := False
    io.fetchBus.fetchCmd.payload.pc := preFetch.pc

    fetchRequest.fetchCmd.valid := preFetch.fetchValid
    fetchRequest.fetchCmd.io := fetchRequest.fetchCmd.pc(31 downto 28) === itcmRange
    fetchRequest.fetchCmd.pc := preFetch.pc

    when(fetchRequest.fetchCmd.io){
      itcm.io.request <> fetchRequest
    }.otherwise{
      io.fetchBus <> fetchRequest
    }
    io.fetchOut.valid := fetchRequest.fetchRsp.fire
    io.fetchOut.instruction := fetchRequest.fetchRsp.instruction
    io.fetchOut.pc := fetchRequest.fetchRsp.pc
  }

  val whiteBox = ifGen(p.whiteBox) {
    new Area {
      /* add some simPublic() signals */
      for(idx <- 0 until p.itcmParameters.TCMBlock){
        Fetch.itcm.banks(idx) simPublic()
      }
    }
  }

}