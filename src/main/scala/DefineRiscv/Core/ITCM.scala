package DefineRiscv.Core

import spinal.core._
import spinal.lib._
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}


case class ITCMParameters(TCMBlock:Int = 4,
                          TCMDepth:Int = 1024,
                          Async:Boolean = true,
                          withFlush:Boolean = true){
  def TCMSize = TCMBlock * TCMDepth
}

// Todo optimal the itcm
// Todo simulation

class ITCM(p:coreParameters,itcm:ITCMParameters) extends PrefixComponent{
  import p._
  import itcm._

  val io = new Bundle{
    val request = slave(FetchBus(p))
    val error = out Bool()
    val flush = ifGen(withFlush) {in Bool()}
  }
  /* distribute ram */
  val addrWidth = log2Up(TCMDepth)
  val last = (io.request.fetchCmd.pc(11).asBits === B"1") && (io.request.fetchCmd.pc(10).asBits === B"1")
  val third = (io.request.fetchCmd.pc(11).asBits === B"1") && (io.request.fetchCmd.pc(10).asBits === B"0")
  val first = (io.request.fetchCmd.pc(9).asBits === B"1")

  val idx = Mux(last,U(3),Mux(third,U(2),Mux(first,U(1),U(0))))
  val align = RegInit(True)
  val pcValue = RegNextWhen(io.request.fetchCmd.pc,io.request.fetchCmd.fire)
  when(io.request.fetchCmd.fire && io.request.fetchCmd.payload.pc(1 downto 0).asBits =/= B"00"){
    align := False
  }.otherwise{align := True}

  io.request.fetchCmd.ready := !io.flush

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
  io.request.fetchRsp.payload.instruction := read.banksValue.map(v => v.data0 ## v.data1 ## v.data2 ## v.data3).read(idx)
  io.error := (io.request.fetchCmd.fire && !io.request.fetchCmd.io) || !align || (io.flush && io.request.fetchCmd.fire)  /* the request is illegal */
  io.request.fetchRsp.valid := !io.error && RegNext(io.request.fetchCmd.fire)

  /* no arbitration for read and flush at same time so carefully flush it */
  val flushIt = new Area{
    val counter = Counter(TCMDepth)
      when(io.flush){
        counter.increment()
      }
      for(bank <- banks){
        bank.write(counter,B"0".resize(8),enable = io.flush)
      }
  }
}

object ITCM extends App{
  val rtl = new RtlConfig().GenRTL(top = new ITCM(coreParameters(),ITCMParameters()))
}