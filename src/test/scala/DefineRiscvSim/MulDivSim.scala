package DefineRiscvSim

import DefineRiscv.Core.backend._
import DefineRiscv.Core.coreParameters
import DefineSim.SIMCFG
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import Untils.SimUntils._
import DefineSim.Logger._
import DefineRiscv.ALU._
import DefineSim.SimUntils._

class MulDivSim extends AnyFunSuite {
  test("simple mul and div ") {
    SIMCFG(compress = true).compile {
      val dut = new SimpleMulDivPlugin(coreParameters())
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)

        val operationList = List(MUL, MULH, MULHSU, MULHU, REM, REMU, DIV, DIVU)

        def check(iter: Int = 100, logIt: Boolean = false) = {
          for (idx <- 0 until iter) {
            val randFunc = getOneRandomValue(operationList)
            dut.io.valid #= true
            val op1 = generateOP(32)
            val op2 = generateOP(32)
            dut.io.op1 #= BigInt(op1, 2)
            dut.io.op2 #= BigInt(op2, 2)
            dut.io.alu #= randFunc
            dut.clockDomain.waitSampling()
            if (logIt) {
              println("op1 : " + binaryComplementEncode(op1))
              println("op2 : " + binaryComplementEncode(op2))
            }

            randFunc match {
              case MUL => {
                val ref = (binaryComplementEncode(op1) * binaryComplementEncode(op2)).toLong.toBinaryString
                val res = dut.io.res.toLong.toBinaryString
                assert(HexStringWithWidth(subString(ref, 0, 32),32) == HexStringWithWidth(res, 32),"MUL error")
              }
              case MULH => {
                val ref = (binaryComplementEncode(op1) * binaryComplementEncode(op2)).toLong.toBinaryString
                val res = dut.io.res.toLong.toBinaryString
                assert(ref.substring(0,res.length) == res,"MULHU error")
              }

              case MULHU => {
                val ref = (BigInt(op1, 2) * BigInt(op2, 2)).toLong.toBinaryString
                val res = dut.io.res.toLong.toBinaryString
                assert(ref.substring(0,res.length) == res,"MULHU error")
              }
              case DIV =>{
                val ref = (binaryComplementEncode(op1) / binaryComplementEncode(op2)).toInt.toBinaryString
                val res = dut.io.res.toLong.toBinaryString
                assert(res == ref,"DIV error")
              }
              case DIVU =>{
                val ref = (BigInt(op1,2) / BigInt(op2,2)).toInt.toBinaryString
                val res = dut.io.res.toLong.toBinaryString
                assert(res == ref, "DIVU error")
              }
              case REM => {
                val ref = (binaryComplementEncode(op1) % binaryComplementEncode(op2)).toInt.toBinaryString
                val res = dut.io.res.toLong.toBinaryString
                assert(res == ref, "REM error")
              }
              case REMU => {
                val ref = (BigInt(op1,2) % BigInt(op2,2)).toInt.toBinaryString
                val res = dut.io.res.toLong.toBinaryString
                assert(res == ref, "REMU error")
              }

              case _ =>

            }

          }
        }
          check(10000, false)

    }
  }
}
