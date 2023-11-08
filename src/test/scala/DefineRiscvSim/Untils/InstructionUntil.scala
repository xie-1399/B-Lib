package DefineRiscvSim.Untils

import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import DefineSim.Logger.{CreateloggerFile, HexStringWithWidth}
/* here is some simple instructions generator */

class OutPut() {
  val inst = ArrayBuffer[String]()
  val instString = ArrayBuffer[String]()
  val instValue = ArrayBuffer[Long]()
}

object InstructionUntil {

  def log(path:String = "./instruction.log",clear:Boolean = true,
          inst:ArrayBuffer[String],instString:ArrayBuffer[String]) = {
    assert(inst.length == instString.length)
    val logger = CreateloggerFile(path, clear)
    for(idx <- 0 until inst.length){
      logger.write(binaryToHexString(inst(idx)) + "\t" + instString(idx) + "\t" + inst(idx) + "\n")
    }
    logger.close()
  }

  def binaryToHexString(value:String) = {
    val res = HexStringWithWidth(Integer.parseInt(value,2).toHexString,8)
    res
  }

  def binaryToBigInt(value: String): Long = {
    val res = Integer.parseInt(value, 2).toLong
    res
  }

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
      val inst = func7 + HexStringWithWidth(rs2.toBinaryString,5) + HexStringWithWidth(rs1.toBinaryString,5) +
        HexStringWithWidth(funct3.toBinaryString,3) + HexStringWithWidth(rd.toBinaryString,5) + opcode
      (func7, funct3) match {
        case ("0000000", 0) => {
          output.inst += inst
          output.instString += s"ADD x${rd},x${rs1},x${rs2}"
          output.instValue += binaryToBigInt(inst)
        }
        case ("0100000", 0) => {
          output.inst += inst
          output.instString += s"SUB x${rd},x${rs1},x${rs2}"
          output.instValue += binaryToBigInt(inst)
        }
        case ("0000000", 4) => {
          output.inst += inst
          output.instString += s"XOR x${rd},x${rs1},x${rs2}"
          output.instValue += binaryToBigInt(inst)
        }
        case ("0000000", 6) => {
          output.inst += inst
          output.instString += s"OR x${rd},x${rs1},x${rs2}"
          output.instValue += binaryToBigInt(inst)
        }
        case ("0000000", 7) => {
          output.inst += inst
          output.instString += s"AND x${rd},x${rs1},x${rs2}"
          output.instValue += binaryToBigInt(inst)
        }
        case ("0000000", 1) => {
          output.inst += inst
          output.instString += s"SLL x${rd},x${rs1},x${rs2}"
          output.instValue += binaryToBigInt(inst)
        }
        case ("0000000", 5) => {
          output.inst += inst
          output.instString += s"SRL x${rd},x${rs1},x${rs2}"
          output.instValue += binaryToBigInt(inst)
        }
        case ("0100000", 5) => {
          output.inst += inst
          output.instString += s"SRA x${rd},x${rs1},x${rs2}"
          output.instValue += binaryToBigInt(inst)
        }
        /* for the slu and sltu compares whether is signed */
        case ("0000000", 2) => {
          output.inst += inst
          output.instString += s"SLT x${rd},x${rs1},x${rs2}"
          output.instValue += binaryToBigInt(inst)
        }
        case ("0000000", 3) => {
          output.inst += inst
          output.instString += s"SLTU x${rd},x${rs1},x${rs2}"
          output.instValue += binaryToBigInt(inst)
        }
        case (_,_) =>
      }
    }
    output
  }

  def RandomIR(iter:Int = 100 ,logFile:Boolean = true) = {
    val outPut = IRGen(iter)
    /* log the instruction out */
    if(logFile) log(inst = outPut.inst,instString = outPut.instString)
    outPut
  }
  val instructionString = List[String]("add a5,a5,a2", "sub a5,a5,a3")

  val instructionList = List(0x00c787b3l, 0x40d787b3l)


}
