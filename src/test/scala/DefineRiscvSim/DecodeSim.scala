package DefineRiscvSim

/* add the decode simulation here */

import DefineSim._
import DefineSim.SimUntils._
import DefineSim.SpinalSim.ScoreBoardSimulation
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import DefineRiscv.Core.frontend._
import DefineRiscv.Core._
import DefineRiscvSim.Untils.GenMIR
import spinal.lib.sim.StreamDriver
import Untils._

/* for the decode purpose */

class DecodeSim extends AnyFunSuite {
  /* the M and I R seems ready in the Decode */
  test("decode sim") {
    SIMCFG(compress = true).compile {
      val dut = new Decode(coreParameters())
      dut
    }.doSimUntilVoid {
      dut =>
        /* Test R type */
        dut.clockDomain.forkStimulus(10)
        dut.io.rs1Data.randomize()
        dut.io.rs2Data.randomize()
        dut.io.decodeOut.ready #= true
        /* assert the condition if right */
        def checkOut(gen: OutPut, index: Int) = {
          assert(gen.illegal(index - 1) == dut.io.decodeOut.ctrl.illegal.toBoolean, " illegal not match ")
          assert(gen.users1(index - 1) == dut.io.decodeOut.ctrl.useRs1.toBoolean, " users1 not match ")
          assert(gen.users2(index - 1) == dut.io.decodeOut.ctrl.useRs2.toBoolean, " users2 not match ")
          assert(gen.userd(index - 1) == dut.io.decodeOut.ctrl.useRd.toBoolean, " use rd not match ")
          assert(gen.jump(index - 1) == dut.io.decodeOut.ctrl.jump.toBoolean, " jump not match ")
          assert(gen.fencei(index - 1) == dut.io.decodeOut.ctrl.fencei.toBoolean, " fencei not match ")
          assert(gen.compress(index - 1) == dut.io.decodeOut.ctrl.compress.toBoolean, " compress not match ")
          assert(gen.branch(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.branch), " branch not match ")
          assert(gen.rs1(index - 1) == dut.io.decodeOut.ctrl.rs1.toBigInt, " rs1 not match ")
          assert(gen.rs2(index - 1) == dut.io.decodeOut.ctrl.rs2.toBigInt, " rs2 not match ")
          assert(gen.rd(index - 1) == dut.io.decodeOut.ctrl.rd.toBigInt, " rd not match ")
          assert(gen.op1(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.op1), " op1 not match ")
          assert(gen.op2(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.op2), " op2 not match ")
          assert(gen.mask(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.mask), " mask not match ")
          assert(gen.alu(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.alu), " alu not match ")
          assert(gen.memoryop(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.memoryOption), " memoryOp not match ")
        }

        def testMIR(logClear:Boolean = true) = {
          val genR = GenMIR.RandomMIR(iter = 10000, ISeed = 0.5,clear = logClear)
          /* the Iseed represent I inst part */
          val length = genR.instValue.length
          var index = 0
          dut.clockDomain.onSamplings(
            if (dut.io.decodeOut.valid.toBoolean) {
              /* check the model */
              checkOut(genR, index)
            }
          )
          /* add R type simulation here*/
          StreamDriver(dut.io.decodeIn, dut.clockDomain) {
            payload =>
              payload.instruction #= genR.instValue(index)
              payload.pc #= index
              index += 1
              true
          }
          dut.clockDomain.waitSamplingWhere(index == length - 1)
        }
        
        def testU(logClear:Boolean = true) = {
          val genU = GenU.RandomU(clear = logClear,iter = 10000)
          val length = genU.instValue.length
          var index = 0
          dut.clockDomain.onSamplings(
            if (dut.io.decodeOut.valid.toBoolean) {
              /* check the model */
              checkOut(genU, index)
            }
          )
          /* add U type simulation here*/
          StreamDriver(dut.io.decodeIn, dut.clockDomain) {
            payload =>
              payload.instruction #= genU.instValue(index)
              payload.pc #= index
              index += 1
              true
          }
          dut.clockDomain.waitSamplingWhere(index == length - 1)
        }
        
        //testMIR()
        testU(logClear = false)
        simSuccess()
    }
  }

}
