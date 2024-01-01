package DefineMem.SRAM.DoublePH

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._
import DefineMem.SRAM.DualPort._
import DefineMem._
import DefineSim.SIMCFG
import scala.collection.mutable
import scala.util.Random

/*  read from the port A , and write from the Port B
* if the CENA is low -> read and the CENB is low -> write into it */

class Sram2PH extends PrefixComponent{
  val io = new Bundle {
    val sramIO = slave(sramWR(wordCount = 32, dataWidth = 92))
  }

  val sram = new CustomMemory(
    wordType = Bits(92 bits),
    wordCount = 32, /* mux * rows*/
    filepath = "src/test/resources/DoublePH/S55NLLG2PH_X32Y1D92.v",
    useDualPort = false,
    withBitWrite = false,
    Multiplexier = 1
  )

  sram.write(io.sramIO.WA, io.sramIO.WD, enable = io.sramIO.valid && io.sramIO.WR)
  io.sramIO.RD := sram.readSync(io.sramIO.RA, enable = io.sramIO.valid && !io.sramIO.WR, readUnderWrite = writeFirst)
}


object Sram2PH extends App{
  import spinal.core.sim._

  val rtl = new RtlConfig().GenRTL(new Sram2PH())

  SIMCFG().compile {
    val dut = new Sram2PH()
    dut
  }.doSimUntilVoid {
    dut =>
      dut.clockDomain.forkStimulus(10000)
      dut.io.sramIO.RA #= 0
      dut.io.sramIO.WA #= 0
      dut.io.sramIO.WD #= 0
      dut.io.sramIO.WR #= false
      dut.io.sramIO.valid #= false
      dut.clockDomain.waitSampling(10)
      val writeDQueue = mutable.Queue[Int]()

      for (idx <- 0 until 32) {
        val writeD = Random.nextInt(4096 * 16 * 10)
        writeDQueue.enqueue(writeD)
        dut.io.sramIO.valid #= true
        dut.io.sramIO.WR #= true
        dut.io.sramIO.WD #= writeD
        dut.io.sramIO.WA #= idx
        dut.clockDomain.waitSampling()
      }

      dut.io.sramIO.valid #= false
      dut.clockDomain.waitSampling(10)
      for (idx <- 0 until 32) {
        dut.io.sramIO.valid #= true
        dut.io.sramIO.WR #= false
        dut.io.sramIO.RA #= idx
        dut.clockDomain.waitSampling()
        dut.io.sramIO.valid #= false
        dut.clockDomain.waitSampling()
        assert(dut.io.sramIO.RD.toBigInt == writeDQueue.dequeue(), "error")
      }
      println("pass")
      simSuccess()
  }

}