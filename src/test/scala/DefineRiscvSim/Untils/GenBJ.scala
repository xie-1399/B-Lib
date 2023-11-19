package DefineRiscvSim.Untils

import scala.util.Random
import scala.math.pow
import DefineSim.Logger._
import InstructionUntil._

object GenBJ {

  /* the branch type is like this （only leave the B and J type Todo ）
  * like : beq a5,s3,40000ab4 */

  val output = new OutPut()

  def BJGen(iter: Int = 100) = {

    for (idx <- 0 until iter) {
      /* the B type jump is with some conditions */
      if(Random.nextInt(100) > 50){
        val rs1 = Random.nextInt(32)
        val rs2 = Random.nextInt(32)
        val imm = Random.nextInt((pow(2, 13) - 1).toInt)
        val immHex = imm.toLong.toHexString
        val immBinary = HexStringWithWidth(imm.toBinaryString,13)
        val func7 = immBinary(0) + immBinary.substring(2,8)
        val rdBinary = immBinary.substring(8,12) + immBinary(1)
        val rd = Integer.parseInt(rdBinary,2)
        val opcode = s"1100011"
        val func3 = Random.nextInt(8)
        val inst = func7 + HexStringWithWidth(rs2.toBinaryString, 5) + HexStringWithWidth(rs1.toBinaryString, 5) +
          HexStringWithWidth(func3.toBinaryString, 3) + rdBinary + opcode
        func3 match {
          case 0 =>{
            val format = List(inst, true, true, true, false, false, false, false, 0, s"BEQ x${rs1},x${rs2},0x${immHex}",
              binaryToBigInt(inst), rs1, rs2, rd, 1, 2, 15, 0, 0)
            assignAll(format, output)
          }
          case 1 => {
            val format = List(inst, true, true, true, false, false, false, false, 1, s"BNE x${rs1},x${rs2},0x${immHex}",
              binaryToBigInt(inst), rs1, rs2, rd, 1, 2, 15, 0, 0)
            assignAll(format, output)
          }
          case 4 => {
            val format = List(inst, true, true, true, false, false, false, false, 4, s"BLT x${rs1},x${rs2},0x${immHex}",
              binaryToBigInt(inst), rs1, rs2, rd, 1, 2, 15, 0, 0)
            assignAll(format, output)
          }
          case 5 => {
            val format = List(inst, true, true, true, false, false, false, false, 5, s"BGE x${rs1},x${rs2},0x${immHex}",
              binaryToBigInt(inst), rs1, rs2, rd, 1, 2, 15, 0, 0)
            assignAll(format, output)
          }
          case 6 => {
            val format = List(inst, true, true, true, false, false, false, false, 6, s"BLTU x${rs1},x${rs2},0x${immHex}",
              binaryToBigInt(inst), rs1, rs2, rd, 1, 2, 15, 0, 0)
            assignAll(format, output)
          }
          case 7 => {
            val format = List(inst, true, true, true, false, false, false, false, 7, s"BGEU x${rs1},x${rs2},0x${immHex}",
              binaryToBigInt(inst), rs1, rs2, rd, 1, 2, 15, 0, 0)
            assignAll(format, output)
          }
          case _ =>
        }
      }

      else{
        /* using the jal */
        if(Random.nextInt(10) > 5){
          val rd = Random.nextInt(32)
          val imm = Random.nextInt((pow(2, 21) - 1).toInt)
          val immBinary = HexStringWithWidth(Random.nextInt((pow(2, 21) - 1).toInt).toBinaryString,21)
          val immHex = imm.toLong.toHexString
          val immInst = immBinary(0) + immBinary.substring(11,21) +  immBinary(10) + immBinary.substring(1,9)
          val opcode = s"1101111"
          val inst = immInst + HexStringWithWidth(rd.toBinaryString,5) + opcode
          val rs1 = Integer.parseInt(inst.substring(12, 17), 2)
          val rs2 = Integer.parseInt(inst.substring(7, 12), 2)
          val format = List(inst, true, false, false, true, true, false, false, 8, s"JAL x${rd},0x${immHex}",
            binaryToBigInt(inst), rs1, rs2, rd, 1, 3, 15, 0, 0)
          assignAll(format, output)
        }
        /* jalr Rd,offset(rs1)*/
        else{
          val rd = Random.nextInt(32)
          val opcode = s"1100111"
          val rs1 = Random.nextInt(32)
          /*get the random u type value */
          val imm = Random.nextInt((pow(2, 12) - 1).toInt)
          val func3 = s"000"
          val immBinary = imm.toBinaryString
          // val immHex = HexStringWithWidth(imm.toLong.toHexString, 3)
          val immString = imm.toLong.toHexString
          val inst = HexStringWithWidth(immBinary, 12) + HexStringWithWidth(rs1.toBinaryString, 5) +
            func3 + HexStringWithWidth(rd.toBinaryString, 5) + opcode
          val rs2 = Integer.parseInt(inst.substring(7, 12), 2)
          val format = List(inst, true, true, false, true, true, false, false, 3, s"JALR x${rd},0x${immString}(x${rs1})",
            binaryToBigInt(inst), rs1, rs2, rd, 0, 0, 15, 0, 0)
          assignAll(format, output)
        }
      }
    }
    output
  }

  def RandomBJ(iter: Int = 100, logFile: Boolean = true, clear: Boolean = true) = {
    val outPut = BJGen(iter)
    /* log the instruction out */
    if (logFile) log(inst = outPut.inst, instString = outPut.instString, clear = clear)
    outPut
  }

}
