package DefineRiscvSim.Untils
import scala.util.Random
import scala.math.pow
import DefineSim.Logger._
import InstructionUntil._
object GenU {
  /* the U type is often used to trans imm data and jump the pc every where
  * examples:
  * (1) the lui inst
  * lui rd,imm  -> lui x1,0x12345 -> will load the 0x12345000 to the x1 reg
  *
  * (2) the auipc inst examples are like this
  * the step is (1) signal extend of the imm (2) pc << 12(fill zero) (3) add them
  * like auipc x2,0xfff -> the pc will be 0x00fff004(if pc equals 4 init)
  * */
  val output = new OutPut()

  def UGen(iter: Int = 100) = {
    for (idx <- 0 until iter) {
      val rd = Random.nextInt(32)
      val randomOp = Random.nextInt(2)
      val opCodes = List(s"0110111",s"0010111")
      val opcode = opCodes(randomOp) /*get the random u type value */
      val imm = Random.nextInt((pow(2,20)-1).toInt)
      val immBinary = imm.toBinaryString
      val immHex = HexStringWithWidth(imm.toLong.toHexString,5)
      val inst = HexStringWithWidth(immBinary,20) + HexStringWithWidth(rd.toBinaryString, 5) + opcode
      val rs1 = Integer.parseInt(inst.substring(12,17),2)
      val rs2 = Integer.parseInt(inst.substring(7,12),2)

      opcode match {
        case "0110111" => {
          val format = List(inst, true, false, false, true, false, false, false, 8, s"LUI x${rd},0x${immHex}",
            binaryToBigInt(inst), rs1, rs2, rd, 0, 4, 15, 15, 0)
          assignAll(format, output)
        }
        case "0010111" => {
          val format = List(inst, true, false, false, true, false, false, false, 8, s"AUIPC x${rd},0x${immHex}",
            binaryToBigInt(inst), rs1, rs2, rd, 1, 4, 15, 0, 0)
          assignAll(format,output)
        }
        case _ =>
      }
    }
    output
  }

  def RandomU(iter: Int = 100, logFile: Boolean = true, clear: Boolean = true) = {
    val outPut = UGen(iter)
    /* log the instruction out */
    if (logFile) log(inst = outPut.inst, instString = outPut.instString, clear = clear)
    outPut
  }


}
