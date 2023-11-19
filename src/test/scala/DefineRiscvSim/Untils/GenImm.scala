package DefineRiscvSim.Untils

import scala.util.Random
import scala.math.pow
import DefineSim.Logger._
import InstructionUntil._

object GenImm {
  /* generate the I_I type instruction
  * the IMM used examples like : ADDI rd,rs1,imm */
  val output = new OutPut()

  def IGen(iter: Int = 100) = {
    for (idx <- 0 until iter) {
      /* not the shift logic function */
      if (Random.nextInt(9) > 3) {
        val rd = Random.nextInt(32)
        val opcode = s"0010011"
        val rs1 = Random.nextInt(32)
        /*get the random u type value */
        val imm = Random.nextInt((pow(2, 12) - 1).toInt)
        val func3 = Random.nextInt(8)
        val immBinary = imm.toBinaryString
        // val immHex = HexStringWithWidth(imm.toLong.toHexString, 3)
        val immString = imm.toLong
        val inst = HexStringWithWidth(immBinary, 12) + HexStringWithWidth(rs1.toBinaryString,5) +
          HexStringWithWidth(func3.toBinaryString,3) + HexStringWithWidth(rd.toBinaryString,5) + opcode
        val rs2 = Integer.parseInt(inst.substring(7, 12), 2)

        func3 match {
          case 0 => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"ADDI x${rd},x${rs1},${immString}",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 15, 0, 0)
            assignAll(format, output)
          }
          case 4 => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"XORI x${rd},x${rs1},${immString}",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 15, 4, 0)
            assignAll(format, output)
          }
          case 6 => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"ORI x${rd},x${rs1},${immString}",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 15, 6, 0)
            assignAll(format, output)
          }
          case 7 => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"ANDI x${rd},x${rs1},${immString}",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 15, 7, 0)
            assignAll(format, output)
          }
          case 2 => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"SLTI x${rd},x${rs1},${immString}",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 15, 2, 0)
            assignAll(format, output)
          }
          case 3 => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"SLTIU x${rd},x${rs1},${immString}",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 15, 3, 0)
            assignAll(format, output)
          }
          case _ =>
        }
      }

      else{
        /* shift logic inst */
        val rd = Random.nextInt(32)
        val opcode = s"0010011"
        val rs1 = Random.nextInt(32)
        /*get the random u type value */
        val func3List = List(s"001",s"101")
        val func7List = List(s"0000000",s"0100000")

        val func3 = func3List(Random.nextInt(2))
        val func7 = func7List(Random.nextInt(2))
        val imm = Random.nextInt(32)

        val inst = func7 + HexStringWithWidth(imm.toBinaryString,5) + HexStringWithWidth(rs1.toBinaryString, 5) +
          func3 + HexStringWithWidth(rd.toBinaryString, 5) + opcode
        val rs2 = Integer.parseInt(inst.substring(7, 12), 2)
        (func3,func7) match {
          case ("001","0000000") => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"SLLI x${rd},x${rs1},${imm}",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 15, 1, 0)
            assignAll(format, output)
          }
          case ("101", "0000000") => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"SRLI x${rd},x${rs1},${imm}",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 15, 5, 0)
            assignAll(format, output)
          }
          case ("101", "0100000") => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"SRAI x${rd},x${rs1},${imm}",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 15, 13, 0)
            assignAll(format, output)
          }
          case _ =>
        }
      }
    }
    output
  }

  def RandomI(iter: Int = 100, logFile: Boolean = true, clear: Boolean = true) = {
    val outPut = IGen(iter)
    /* log the instruction out */
    if (logFile) log(inst = outPut.inst, instString = outPut.instString, clear = clear)
    outPut
  }


}
