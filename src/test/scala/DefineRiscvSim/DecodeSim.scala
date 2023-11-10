package DefineRiscvSim

/* add the decode simulation here */
import DefineSim._
import DefineSim.SimUntils._
import DefineSim.SpinalSim.ScoreBoardSimulation
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import DefineRiscv.Core.frontend._
import DefineRiscv.Core._
import DefineRiscvSim.Untils.GenIR
import spinal.lib.sim.StreamDriver

/* for the decode purpose */

class DecodeSim extends AnyFunSuite {

  test("decode IR sim"){
    SIMCFG(compress = true).compile{
      val dut = new Decode(coreParameters())
      dut
    }.doSim{
      dut =>
        /* Test R type */
        dut.clockDomain.forkStimulus(10)
        val genR = GenIR.RandomIR(iter = 100000)
        val length = genR.instValue.length
        var index = 0

        /* assert the condition if right */
        def checkOut() = {
          assert(genR.illegal(index - 1) == dut.io.decodeOut.ctrl.illegal.toBoolean," illegal not match ")
          assert(genR.users1(index - 1) == dut.io.decodeOut.ctrl.useRs1.toBoolean," users1 not match ")
          assert(genR.users2(index - 1) == dut.io.decodeOut.ctrl.useRs2.toBoolean," users2 not match ")
          assert(genR.userd(index - 1) == dut.io.decodeOut.ctrl.useRd.toBoolean," use rd not match ")
          assert(genR.jump(index - 1) == dut.io.decodeOut.ctrl.jump.toBoolean," jump not match ")
          assert(genR.fencei(index - 1) == dut.io.decodeOut.ctrl.fencei.toBoolean," fencei not match ")
          assert(genR.compress(index - 1) == dut.io.decodeOut.ctrl.compress.toBoolean," compress not match ")
          assert(genR.branch(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.branch)," branch not match ")
          assert(genR.rs1(index - 1) == dut.io.decodeOut.ctrl.rs1.toBigInt," rs1 not match ")
          assert(genR.rs2(index - 1) == dut.io.decodeOut.ctrl.rs2.toBigInt," rs2 not match ")
          assert(genR.rd(index - 1) == dut.io.decodeOut.ctrl.rd.toBigInt," rd not match ")
          assert(genR.op1(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.op1)," op1 not match ")
          assert(genR.op2(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.op2)," op2 not match ")
          assert(genR.mask(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.mask)," mask not match ")
          assert(genR.alu(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.alu)," alu not match ")
          assert(genR.memoryop(index - 1) == getEnumEncodingValue(dut.io.decodeOut.ctrl.memoryOption)," memoryOp not match ")
        }

        dut.clockDomain.onSamplings(
          if(dut.io.decodeOut.valid.toBoolean){
            /* check the model */
            checkOut()
          }
        )

        /* add R type simulation here*/
        dut.io.rs1Data.randomize()
        dut.io.rs2Data.randomize()
        dut.io.decodeOut.ready #= true

        StreamDriver(dut.io.decodeIn,dut.clockDomain){
          payload =>
            payload.instruction #= genR.instValue(index)
            payload.pc #= index
            index += 1
            true
        }
        dut.clockDomain.waitSamplingWhere(index == length - 1)
    }
  }

  test(""){

  }


}
