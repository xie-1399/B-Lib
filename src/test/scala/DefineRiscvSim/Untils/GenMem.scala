package DefineRiscvSim.Untils

import scala.util.Random
import scala.math.{pow, random}
import DefineSim.Logger._
import InstructionUntil._

object GenMem {

  /* the Memory option contains load or store type
  * example is lw t0,0(a0)
  * and notice the lbu will use the unsigned extension
  * example of store: sw t0,0(a1) sw rs2,offset(rs1)*/

  val output = new OutPut()

  def MemGen(iter: Int = 100) = {
    /* store it and load it by half */
    for (idx <- 0 until iter) {
      if (Random.nextInt(10) > 5) {
        /* load the memory data in the reg */
        val rd = Random.nextInt(32)
        val func3 = Random.nextInt(6)
        val imm = Random.nextInt((pow(2, 12) - 1).toInt)
        val opcode = s"0000011"
        val immBinary = imm.toBinaryString
        val rs1 = Random.nextInt(32)
        /* get the random u type value */
        val inst = HexStringWithWidth(immBinary, 12) + HexStringWithWidth(rs1.toBinaryString, 5) +
          HexStringWithWidth(func3.toBinaryString, 3) + HexStringWithWidth(rd.toBinaryString, 5) + opcode
        val rs2 = Integer.parseInt(inst.substring(7, 12), 2)
        func3 match {
          case 0 => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"LB x${rd},${imm}(x${rs1})",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 1, 0, 1)
            assignAll(format, output)
          }
          case 1 => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"LH x${rd},${imm}(x${rs1})",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 3, 0, 1)
            assignAll(format, output)
          }
          case 2 => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"LW x${rd},${imm}(x${rs1})",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 15, 0, 1)
            assignAll(format, output)
          }
          case 4 => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"LBU x${rd},${imm}(x${rs1})",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 1, 0, 2)
            assignAll(format, output)
          }
          case 5 => {
            val format = List(inst, true, true, false, true, false, false, false, 8, s"LHU x${rd},${imm}(x${rs1})",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 3, 0, 2)
            assignAll(format, output)
          }
          case _ =>

        }
      }
      else {
        /* generate the store instruction */
        val imm = Random.nextInt((pow(2, 12) - 1).toInt)
        val rs2 = Random.nextInt(32)
        val rs1 = Random.nextInt(32)
        val immBinary = HexStringWithWidth(imm.toBinaryString,12)
        val func7 = immBinary.substring(0,7)
        val rdBinary = immBinary.substring(7,12)
        val rd = Integer.parseInt(immBinary.substring(7,12),2)
        val opcode = s"0100011"
        val func3 = Random.nextInt(3)
        val inst = func7 + HexStringWithWidth(rs2.toBinaryString,5) + HexStringWithWidth(rs1.toBinaryString,5) +
          HexStringWithWidth(func3.toBinaryString,3) + rdBinary + opcode
        func3 match {
          case 0 => {
            val format = List(inst, true, true, true, false, false, false, false, 8, s"SB x${rs2},${imm}(x${rs1})",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 1, 1, 0, 3)
            assignAll(format, output)
          }
          case 1 => {
            val format = List(inst, true, true, true, false, false, false, false, 8, s"SH x${rs2},${imm}(x${rs1})",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 1, 3, 0, 3)
            assignAll(format, output)
          }
          case 2 => {
            val format = List(inst, true, true, true, false, false, false, false, 8, s"SW x${rs2},${imm}(x${rs1})",
              binaryToBigInt(inst), rs1, rs2, rd, 0, 1, 15, 0, 3)
            assignAll(format, output)
          }
          case _ =>
        }

      }
    }
    output
  }

  def RandomMem(iter: Int = 100, logFile: Boolean = true, clear: Boolean = true) = {
    val outPut = MemGen(iter)
    /* log the instruction out */
    if (logFile) log(inst = outPut.inst, instString = outPut.instString, clear = clear)
    outPut
  }


}
