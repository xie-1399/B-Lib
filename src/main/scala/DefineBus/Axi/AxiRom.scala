package DefineBus.Axi

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._
import spinal.core.sim._


object AxiRom{
  def getAxiConfig(dataWidth: Int, byteCount: BigInt, idWidth: Int) = Axi4Config(
    addressWidth = log2Up(byteCount),
    dataWidth = dataWidth,
    idWidth = idWidth,
    useLock = false,
    useRegion = false,
    useCache = false,
    useProt = false,
    useQos = false,
    useResp = false
  )

  def main(args: Array[String]): Unit = {
    val rtl = SpinalVerilog(new AxiRom(32,1024,4).setDefinitionName("AxiRom"))
  }
}

class AxiRom(dataWidth:Int,byteCount : BigInt, idWidth : Int,WhiteBox:Boolean = false) extends Component{
  val axiConfig = AxiRom.getAxiConfig(dataWidth, byteCount, idWidth)
  val io = new Bundle{
    val axi = slave(Axi4ReadOnly(axiConfig))
  }
  noIoPrefix()

  val wordCount = byteCount / axiConfig.bytePerWord
  val ram = Mem(axiConfig.dataType, wordCount.toInt) /* the data bits len == data len */
  val wordRange = log2Up(wordCount) + log2Up(axiConfig.bytePerWord) - 1 downto log2Up(axiConfig.bytePerWord)

  io.axi.ar.ready := True
  io.axi.r.valid := RegNext(io.axi.ar.fire)
  io.axi.r.payload.id := RegNextWhen(io.axi.ar.id,io.axi.ar.fire)
  io.axi.r.payload.last := True

  io.axi.r.payload.data := ram.readSync(
    address = io.axi.ar.addr(wordRange),
    enable = io.axi.ar.fire
  )

  val whiteBox = ifGen(WhiteBox){
    ram.initBigInt((0 until 4096).map(BigInt(_)))
    ram.simPublic()
  }

}
