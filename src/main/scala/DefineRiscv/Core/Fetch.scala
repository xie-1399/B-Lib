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

package DefineRiscv.Core

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._

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

  def toAxi4(): Unit = {

  }
}

case class FetchOut(p:coreParameters) extends Bundle {
  val pc = UInt(p.Xlen bits)
  val instruction = Bits(p.instructionWidth bits)
}

/* fetch need to jump the pc Value */

class Fetch(p:coreParameters) extends PrefixComponent{
  import p._
  val io = new Bundle{
    val halt = in Bool()
    val flush = in Bool()
    val fetchOut = master Stream (FetchOut(p))
  }
  val fetchRequest = FetchBus(p)

  val preFetch = new Area{
    val pc = Reg(UInt(Xlen bits)).init(resetValue)
    val pcNext = if(withRVC) pc + 2 else pc + 4
    val inc = False
    pc := Mux(inc,pcNext,pc) /* if receive increase then pc plus */
    val fetchValid = True.clearWhen(io.halt)
  }

  val Fetch = new Area {

    fetchRequest.fetchCmd.valid := preFetch.fetchValid
    fetchRequest.fetchCmd.io := fetchRequest.fetchCmd.pc(31 downto 28) === ioRange
    fetchRequest.fetchCmd.pc := preFetch.pc

    val fetchOut = Stream(FetchOut(p))
    fetchOut.valid := fetchRequest.fetchRsp.fire
    fetchOut.instruction := fetchRequest.fetchRsp.instruction
    fetchOut.pc := fetchRequest.fetchRsp.pc
  }

}

 object Fetch extends App{
   val rtl = new RtlConfig().GenRTL(new Fetch(coreParameters()))
 }