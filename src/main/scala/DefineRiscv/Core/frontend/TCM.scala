/** *************************************************************************************
 * MIT License
 *
 * Copyright (c) 2023 xxl
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * ************************************************************************************* */
package DefineRiscv.Core.frontend

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._
import DefineRiscv.Core.coreParameters


/* set the distribute big ram which can read and write */

case class TCMParameters(TCMBlock:Int = 4,
                          TCMDepth:Int = 1024,
                          Sync:Boolean = true,
                          withFlush:Boolean = false){
  def TCMSize = TCMBlock * TCMDepth
}

class TCM(p:coreParameters,tcm:TCMParameters) extends PrefixComponent{
  import tcm._

  val io = new Bundle{
    val request = slave(FetchBus(p))
    val error = out Bool()
    val flush = ifGen(withFlush) {in Bool()}
  }
  /* 4 distribute ram */
  val addrWidth = log2Up(TCMDepth)
  val last = (io.request.fetchCmd.pc(addrWidth + 1).asBits === B"1") && (io.request.fetchCmd.pc(addrWidth).asBits === B"1")
  val third = (io.request.fetchCmd.pc(addrWidth + 1).asBits === B"1") && (io.request.fetchCmd.pc(addrWidth).asBits === B"0")
  val first = (io.request.fetchCmd.pc(addrWidth).asBits === B"1")
  val ready = if(withFlush) !io.flush else True
  val idx = if(TCMBlock != 1) RegNextWhen(Mux(last,U(TCMBlock - 1),Mux(third,U(TCMBlock - 2),Mux(first,U(TCMBlock - 3),U(TCMBlock - 4)))),io.request.fetchCmd.fire) else U(0)

  val align = RegInit(True)
  val pcValue = RegNextWhen(io.request.fetchCmd.pc,io.request.fetchCmd.fire)
  when(io.request.fetchCmd.fire && io.request.fetchCmd.payload.pc(1 downto 0).asBits =/= B"00"){
    align := False
  }.otherwise{align := True}

  io.request.fetchCmd.ready := ready

  val banks = Seq.fill(TCMBlock)(Mem(Bits(8 bits),TCMDepth)) /* 4 * 1024 */

  val read = new Area{
        val banksValue = for(bank <- banks) yield new Area {
          val data0 = bank.readSync(io.request.fetchCmd.payload.pc(addrWidth-1 downto 0),enable = io.request.fetchCmd.fire)
          val data1 = bank.readSync(io.request.fetchCmd.payload.pc(addrWidth-1 downto 0) + 1,enable = io.request.fetchCmd.fire)
          val data2 = bank.readSync(io.request.fetchCmd.payload.pc(addrWidth-1 downto 0) + 2,enable = io.request.fetchCmd.fire)
          val data3 = bank.readSync(io.request.fetchCmd.payload.pc(addrWidth-1 downto 0) + 3,enable = io.request.fetchCmd.fire)
        }
  }

  io.request.fetchRsp.payload.pc := pcValue
  io.request.fetchRsp.payload.instruction := read.banksValue.map(v => v.data3 ## v.data2 ## v.data1 ## v.data0).read(idx)
  io.error := (io.request.fetchCmd.fire && !io.request.fetchCmd.io) || !align   /* the request is illegal */
  ifGen(withFlush){
    when((io.flush && io.request.fetchCmd.fire)){
      io.error := True
    }
  }
  io.request.fetchRsp.valid := !io.error && RegNext(io.request.fetchCmd.fire)

  /* no arbitration for read and flush at same time so carefully flush it */
  val flushIt = ifGen(withFlush){
    new Area{
      val counter = Counter(TCMDepth)
      when(io.flush){
        counter.increment()
      }
      for(bank <- banks){
        bank.write(counter,B"0".resize(8),enable = io.flush)
      }
    }
  }

}

object TCM extends App{
  val rtl = new RtlConfig(path = "temp").GenRTL(top = new TCM(coreParameters(),TCMParameters()))
}