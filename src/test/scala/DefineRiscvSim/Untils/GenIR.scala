package DefineRiscvSim.Untils

import DefineRiscvSim.Untils.InstructionUntil.{assignAll, binaryToBigInt, log}
import DefineSim.Logger.HexStringWithWidth
import spinal.lib.cpu.riscv.impl.Utils.BR

import scala.util.Random

object GenIR{

  /* generate the IR type instruction for model check */

  def IRGen(iter: Int = 100) = {
    val output = new OutPut()
    for (idx <- 0 until iter) {
      val func7List = List(s"0000000", s"0100000")
      val rs1 = Random.nextInt(32)
      val rs2 = Random.nextInt(32)
      val rd = Random.nextInt(32)
      val opcode = s"0110011"
      val funct3 = Random.nextInt(8)
      val func7 = func7List(Random.nextInt(2))
      val inst = func7 + HexStringWithWidth(rs2.toBinaryString, 5) + HexStringWithWidth(rs1.toBinaryString, 5) +
        HexStringWithWidth(funct3.toBinaryString, 3) + HexStringWithWidth(rd.toBinaryString, 5) + opcode
      (func7, funct3) match {
        case ("0000000", 0) => {
          val format = List(inst,true,true,true,true,false,false,false,8,s"ADD x${rd},x${rs1},x${rs2}",
            binaryToBigInt(inst),rs1,rs2,rd,0,5,15,0,0)
          assignAll(format,output)
        }
        case ("0100000", 0) => {
          val format = List(inst, true, true, true, true, false, false, false, 8, s"SUB x${rd},x${rs1},x${rs2}",
            binaryToBigInt(inst), rs1, rs2, rd, 0, 5, 15, 8, 0)
          assignAll(format, output)
        }
        case ("0000000", 4) => {
          val format = List(inst, true, true, true, true, false, false, false, 8, s"XOR x${rd},x${rs1},x${rs2}",
            binaryToBigInt(inst), rs1, rs2, rd, 0, 5, 15, 4, 0)
          assignAll(format, output)
        }
        case ("0000000", 6) => {
          val format = List(inst, true, true, true, true, false, false, false, 8, s"OR x${rd},x${rs1},x${rs2}",
            binaryToBigInt(inst), rs1, rs2, rd, 0, 5, 15, 6, 0)
          assignAll(format, output)
        }
        case ("0000000", 7) => {
          val format = List(inst, true, true, true, true, false, false, false, 8, s"AND x${rd},x${rs1},x${rs2}",
            binaryToBigInt(inst), rs1, rs2, rd, 0, 5, 15, 7, 0)
          assignAll(format, output)
        }
        case ("0000000", 1) => {
          val format = List(inst, true, true, true, true, false, false, false, 8, s"SLL x${rd},x${rs1},x${rs2}",
            binaryToBigInt(inst), rs1, rs2, rd, 0, 5, 15, 1, 0)
          assignAll(format, output)
        }
        case ("0000000", 5) => {
          val format = List(inst, true, true, true, true, false, false, false, 8, s"SRL x${rd},x${rs1},x${rs2}",
            binaryToBigInt(inst), rs1, rs2, rd, 0, 5, 15, 5, 0)
          assignAll(format, output)
        }
        case ("0100000", 5) => {
          val format = List(inst, true, true, true, true, false, false, false, 8, s"SRA x${rd},x${rs1},x${rs2}",
            binaryToBigInt(inst), rs1, rs2, rd, 0, 5, 15, 13, 0)
          assignAll(format, output)
        }
        /* for the slu and sltu compares whether is signed */
        case ("0000000", 2) => {
          val format = List(inst, true, true, true, true, false, false, false, 8, s"SLT x${rd},x${rs1},x${rs2}",
            binaryToBigInt(inst), rs1, rs2, rd, 0, 5, 15, 2, 0)
          assignAll(format, output)
        }
        case ("0000000", 3) => {
          val format = List(inst, true, true, true, true, false, false, false, 8, s"SLTU x${rd},x${rs1},x${rs2}",
            binaryToBigInt(inst), rs1, rs2, rd, 0, 5, 15, 3, 0)
          assignAll(format, output)
        }
        case (_, _) =>
      }
    }
    output
  }

  def RandomIR(iter: Int = 100, logFile: Boolean = true) = {
    val outPut = IRGen(iter)
    /* log the instruction out */
    if (logFile) log(inst = outPut.inst, instString = outPut.instString)
    outPut
  }

}
