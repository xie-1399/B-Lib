package DefineMem.SRAM.DualPort

import DefineMem.CustomMemory
import DefineSim.SIMCFG
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._

import scala.collection.mutable
import scala.util.Random

case class sramWR(wordCount:Int = 4096 , dataWidth:Int = 32,maskWidth:Int = 4) extends Bundle with IMasterSlave {
  val RA = UInt(log2Up(wordCount) bits)
  val RD = Bits(dataWidth bits)

  val WA = UInt(log2Up(wordCount) bits)
  val WD = Bits(dataWidth bits)
  val WM = Bits(maskWidth bits)
  val WR = Bool()
  val valid = Bool()

  override def asMaster(): Unit = {
    out(RA,WA,WD,WM,WR,valid)
    in(RD)
  }
}

class DualPortSram extends PrefixComponent{
  val io = new Bundle{
    val sramIO = slave (sramWR())
  }

  val sram = new CustomMemory(
    wordType = Bits(32 bits),
    wordCount = 4096, /* mux * rows*/
    filepath = "src/test/resources/DualPort/S55NLLGDPH_X512Y8D32_BW.v",
    useDualPort = true,
    withBitWrite = true,
    Multiplexier = 8
  )

  sram.write(io.sramIO.WA,io.sramIO.WD,enable = io.sramIO.valid && io.sramIO.WR,mask = io.sramIO.WM)
  io.sramIO.RD := sram.readSync(io.sramIO.RA,enable = io.sramIO.valid && !io.sramIO.WR,readUnderWrite = writeFirst)
}

object DualPortSram extends App{
  /* test the rtl */
  import spinal.core.sim._
  val rtl = new RtlConfig().GenRTL(new DualPortSram())

  SIMCFG().compile{
    val dut = new DualPortSram()
    dut
  }.doSimUntilVoid{
    dut =>
      dut.clockDomain.forkStimulus(10000)

      dut.io.sramIO.WM #= 15
      dut.io.sramIO.RA #= 0
      dut.io.sramIO.WA #= 0
      dut.io.sramIO.WD #= 0
      dut.io.sramIO.WR #= false
      dut.io.sramIO.valid #= false
      dut.clockDomain.waitSampling(10)
      val writeDQueue = mutable.Queue[Int]()

      for(idx <- 0 until 1024){
        val writeD = Random.nextInt(4096 * 16 * 10)
        writeDQueue.enqueue(writeD)
        dut.io.sramIO.valid #= true
        dut.io.sramIO.WR #= true
        dut.io.sramIO.WD #= writeD
        dut.io.sramIO.WM #= 15
        dut.io.sramIO.WA #= idx
        dut.clockDomain.waitSampling()
      }

      dut.io.sramIO.valid #= false
      dut.clockDomain.waitSampling(10)
      for(idx <- 0 until 1024){
        dut.io.sramIO.valid #= true
        dut.io.sramIO.WR #= false
        dut.io.sramIO.RA #= idx
        dut.clockDomain.waitSampling()
        dut.io.sramIO.valid #= false
        dut.clockDomain.waitSampling()
        assert(dut.io.sramIO.RD.toBigInt == writeDQueue.dequeue(),"error")
      }
      println("pass")
      simSuccess()
  }

}