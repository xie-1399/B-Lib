package DefineMath.Arith

import spinal.core._
import spinal.lib._
import DefineSim.SpinalSim._
import scala.util.Random

object GCDSet{
  /* this module implement gcd value between two UInt values */
  class GCD(width: Int) extends PrefixComponent {
    val io = new Bundle {
      val value1 = in UInt (width bits)
      val value2 = in UInt (width bits)
      val load = in Bool()
      val outputValid = out Bool()
      val outputGCD = out UInt (width bits)
    }
    val temp1 = Reg(UInt(width bits)).init(width)
    val temp2 = Reg(UInt(width bits)).init(width + 1)

    when(temp1 > temp2) {
      temp1 := temp1 - temp2
    }.otherwise {
      temp2 := temp2 - temp1
    }

    when(io.load) {
      temp1 := io.value1
      temp2 := io.value2
    }
    /* work until calculate the GCD value */
    io.outputValid := temp2 === 0
    io.outputGCD := temp1
  }

  /* more gcd can add here */
}

object GCD extends App {
  /* simple test in the source file */

  import spinal.core.sim._
  import DefineSim._

  SIMCFG(gtkFirst = true).compile {
    val dut = new GCDSet.GCD(16)
    dut
  }.doSim {
    dut =>
      def gcd(a: Int, b: Int): Int = {
        if (b == 0) a
        else gcd(b, a % b)
      }

      dut.clockDomain.forkStimulus(10)
      dut.io.load #= false
      dut.clockDomain.waitSampling()

      for (idx <- 0 until 1000) {
        val value1 = Random.nextInt(100) + 1
        val value2 = Random.nextInt(100) + 1
        dut.io.value1 #= value1
        dut.io.value2 #= value2
        dut.io.load #= true
        dut.clockDomain.waitSampling()
        dut.io.load #= false
        dut.clockDomain.waitSamplingWhere(dut.io.outputValid.toBoolean)
        assert(dut.io.outputGCD.toInt == gcd(value1, value2), s"left = ${dut.io.outputGCD.toInt},right = ${gcd(value1, value2)}")
      }
      println("success")
  }

}