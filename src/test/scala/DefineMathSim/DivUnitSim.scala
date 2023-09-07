package DefineMathSim
import DefineMath.Divider._
import DefineSim.SpinalSim._
import DefineSim._
import org.scalatest.funsuite.AnyFunSuite
import spinal.core
import spinal.core._
import spinal.sim._
import spinal.core.sim._
import spinal.lib.math.SignedDivider

import scala.util.Random
import scala.math._


class DivUnitSim extends AnyFunSuite {

  test("unsigned div"){
    SIMCFG(gtkFirst = true).compile{
      val dut = new UnsignDivUnit(32,32,false)
      dut
    }.doSim{
      dut =>
        dut.clockDomain.forkStimulus(10)
        dut.io.cmd.valid #= false
        dut.clockDomain.waitSampling(3)
        def setValue(nume:Int,demo:Int) = {
          dut.io.flush #= false
          dut.io.cmd.valid #= true
          dut.io.cmd.payload.numerator #= nume
          dut.io.cmd.payload.denominator #= demo
          dut.clockDomain.waitSampling()
          dut.io.cmd.valid #= false
          dut.clockDomain.waitSamplingWhere(dut.io.rsp.valid.toBoolean)
          assert(dut.io.rsp.payload.quotient.toLong == nume / demo)
          assert(dut.io.rsp.payload.remainder.toLong == nume % demo)
        }

        def testRandom(testCase:Int = 100) = {
          for (idx <- 0 until testCase){
            val nume = Random.nextInt(500)
            val demo = Random.nextInt(500) + 1
            setValue(nume, demo)
          }
        }
        testRandom()
    }
  }

  test("signed div") {
    SIMCFG(gtkFirst = true).compile {
      val dut = new SignDivUnit(32, 32, false)
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        dut.io.cmd.valid #= false
        dut.clockDomain.waitSampling(3)

        def setValue(nume: Int, demo: Int) = {
          dut.io.flush #= false
          dut.io.cmd.valid #= true
          dut.io.cmd.payload.numerator #= nume
          dut.io.cmd.payload.denominator #= demo
          dut.clockDomain.waitSampling()
          dut.io.cmd.valid #= false
          dut.clockDomain.waitSamplingWhere(dut.io.rsp.valid.toBoolean)
          assert(dut.io.rsp.payload.quotient.toLong == nume / demo)
          assert(dut.io.rsp.payload.remainder.toLong == nume % demo)
        }

        def testRandom(testCase: Int = 100) = {
          for (idx <- 0 until testCase) {
            val nume = Random.nextInt(20) - 10
            val demo = Random.nextInt(10) - 10
            setValue(nume, demo)
          }
        }
        testRandom()
    }
  }

  test("Mixed div") {
    SIMCFG(gtkFirst = true).compile {
      val dut = new MixedDivider(32, 32, false)
      dut
    }.doSim{
      dut =>
        dut.clockDomain.forkStimulus(10)
        dut.io.cmd.valid #= false
        dut.clockDomain.waitSampling(3)

        def setValue(nume: Int, demo: Int) = {
          dut.io.flush #= false
          dut.io.cmd.valid #= true
          dut.io.cmd.payload.numerator #= nume
          dut.io.cmd.payload.denominator #= demo
          dut.io.cmd.signed #= false
          dut.clockDomain.waitSampling()
          dut.io.cmd.valid #= false
          dut.clockDomain.waitSamplingWhere(dut.io.rsp.valid.toBoolean)
          assert(dut.io.rsp.payload.quotient.toLong == nume / demo)
          assert(dut.io.rsp.payload.remainder.toLong == nume % demo)
        }

        def testRandom(testCase: Int = 100) = {
          for (idx <- 0 until testCase) {
            val nume = Random.nextInt(500)
            val demo = Random.nextInt(500) + 1
            setValue(nume, demo)
          }
        }

        testRandom()
    }

  }

}
