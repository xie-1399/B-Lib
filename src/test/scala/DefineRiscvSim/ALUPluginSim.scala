package DefineRiscvSim

import DefineSim.SIMCFG
import org.scalatest.funsuite.AnyFunSuite

import spinal.core.sim._
import DefineRiscv.Core.backend.ALUPlugin
import DefineRiscv.Core._
import DefineRiscv.ALU._
import scala.util.Random
import DefineSim.Logger._
import Untils.SimUntils._

class ALUPluginSim extends AnyFunSuite {
  /* test finish and some untils can be reuse and alu is ready */
  test("random test about the alu") {
    SIMCFG(compress = true).compile {
      val dut = new ALUPlugin(coreParameters())
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)

        val Alu = List(ADD, SUB, SRA, SLT, SLTU, SRL, SLL, AND, OR, XOR,COPY)

        def matchIt(iter:Int,logIt:Boolean = false) = {
          for(idx <- 0 until iter){

            dut.io.valid #= true
            val randFunc = Random.nextInt(11)
            dut.io.alu #= Alu(randFunc)
            val op1 = generateOP(32, over = true)
            val op2 = if(randFunc == 0) generateOP(32, bias = true) else generateOP(32, over = true)

            dut.io.op1 #= BigInt(op1, 2)
            val shifter = randFunc == 2 || randFunc == 5 || randFunc == 6
            val realop = if(shifter) op2.substring(27,32) else op2
            dut.io.op2 #= BigInt(realop, 2)
            val compare = if(randFunc == 3) binaryComplementEncode(op1) < binaryComplementEncode(op2) else BigInt(op1,2) < BigInt(op2,2)
            val judge = if(compare) 1 else 0
            dut.clockDomain.waitSampling()

            if(logIt){
              println("op1 : " + binaryComplementEncode(op1))
              println("op2 : " + binaryComplementEncode(op2))
              println("res : " + binaryComplementEncode(HexStringWithWidth(dut.io.res.toLong.toBinaryString,32)))
            }
            randFunc match {
              case 0 => assert(binaryComplementEncode(HexStringWithWidth(dut.io.res.toLong.toBinaryString,32)) == binaryComplementEncode(op1) + binaryComplementEncode(op2), "ADD wrong")
              case 1 => assert(binaryComplementEncode(HexStringWithWidth(dut.io.res.toLong.toBinaryString,32)) == binaryComplementEncode(op1) - binaryComplementEncode(op2), "SUB wrong")
              case 2 => assert(HexStringWithWidth(dut.io.res.toLong.toBinaryString,32) == shift(op1,32,Integer.parseInt(realop,2),false,false),"SRA wrong")
              case 3 => assert(dut.io.res.toBigInt == judge,"SLT wrong")
              case 4 => assert(dut.io.res.toBigInt == judge,"SLTU wrong")
              case 5 => assert(HexStringWithWidth(dut.io.res.toLong.toBinaryString,32) == shift(op1,32,Integer.parseInt(realop,2),false,true),"SRL wrong")
              case 6 => assert(HexStringWithWidth(dut.io.res.toLong.toBinaryString,32) == shift(op1,32,Integer.parseInt(realop,2)),"SLL wrong")
              case 7 => assert(dut.io.res.toBigInt == (BigInt(op1, 2) & BigInt(op2, 2)), "OR wrong")
              case 8 => assert(dut.io.res.toBigInt == (BigInt(op1, 2) | BigInt(op2, 2)), "XOR wrong")
              case 9 => assert(dut.io.res.toBigInt == (BigInt(op1, 2) ^ BigInt(op2, 2)), "AND wrong")
              case _ => assert(dut.io.res.toBigInt == dut.io.op1.toBigInt,"Copy it wrong")
            }
          }
        }
        matchIt(10000,logIt = false)
    }
  }
}
