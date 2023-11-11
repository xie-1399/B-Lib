//package DefineRiscvSim.Untils
//
//import scala.util.Random
//import scala.math.pow
//import DefineSim.Logger._
//import InstructionUntil._
//
//object GenBJ {
//
//  /* the branch type is like this （only leave the B and J type Todo ）
//  * like : beq a5,s3,40000ab4 */
//
//  val output = new OutPut()
//
//  def BJGen(iter: Int = 100) = {
//    for (idx <- 0 until iter) {
//
//      val rs1 = Random.nextInt(32)
//      val rs2 = Random.nextInt(32)
//
//      val rd = Random.nextInt(32)
//      val randomOp = Random.nextInt(2)
//      val opCodes = List(s"0110111", s"0010111")
//      val opcode = opCodes(randomOp)
//      /*get the random u type value */
//      val imm = Random.nextInt((pow(2, 20) - 1).toInt)
//      val immBinary = imm.toBinaryString
//      val immHex = HexStringWithWidth(imm.toLong.toHexString, 5)
//      val inst = HexStringWithWidth(immBinary, 20) + HexStringWithWidth(rd.toBinaryString, 5) + opcode
//      val rs1 = Integer.parseInt(inst.substring(12, 17), 2)
//      val rs2 = Integer.parseInt(inst.substring(7, 12), 2)
//
//      opcode match {
//        case "0110111" => {
//          val format = List(inst, true, false, false, true, false, false, false, 8, s"LUI x${rd},0x${immHex}",
//            binaryToBigInt(inst), rs1, rs2, rd, 0, 4, 15, 15, 0)
//          assignAll(format, output)
//        }
//        case "0010111" => {
//          val format = List(inst, true, false, false, true, false, false, false, 8, s"AUIPC x${rd},0x${immHex}",
//            binaryToBigInt(inst), rs1, rs2, rd, 1, 4, 15, 0, 0)
//          assignAll(format, output)
//        }
//        case _ =>
//      }
//    }
//    output
//  }
//
//}
