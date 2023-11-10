package DefineRiscvSim.Untils

import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import DefineSim.Logger.{CreateloggerFile, HexStringWithWidth}
import spinal.core._
import DefineRiscv._
/* here is some simple instructions generator */

class OutPut() {
  val inst = ArrayBuffer[String]()
  val illegal = ArrayBuffer[Boolean]()
  val users1 = ArrayBuffer[Boolean]()
  val users2 = ArrayBuffer[Boolean]()
  val userd = ArrayBuffer[Boolean]()
  val jump = ArrayBuffer[Boolean]()
  val fencei = ArrayBuffer[Boolean]()
  val compress = ArrayBuffer[Boolean]()
  val branch = ArrayBuffer[Int]()
  val instString = ArrayBuffer[String]()
  val instValue = ArrayBuffer[Long]()
  val rs1 = ArrayBuffer[Int]()
  val rs2 = ArrayBuffer[Int]()
  val rd = ArrayBuffer[Int]()
  val op1 = ArrayBuffer[Int]() /* pc:1 rs1:0*/
  val op2 = ArrayBuffer[Int]() /* IMM_I,IMM_S,IMM_B,IMM_J,IMM_U,RS2 */
  val mask = ArrayBuffer[Int]() /* word : 15 half : 3 byte : 1*/
  val alu = ArrayBuffer[Int]() /* see the alu map */
  val memoryop = ArrayBuffer[Int]() /* not:0 load:1 load_U : 2 store : 3*/
}


object InstructionUntil {

  val branchMap = Map(
    "EQ" -> 0,
    "NE" -> 1,
    "J" -> 2,
    "JR" -> 3,
    "LT" -> 4,
    "GE" -> 5,
    "LTU" -> 6,
    "GEU" -> 7,
    "N" -> 8
  )

  val aluMap = Map(
    "ADD "-> 0,
    "SLL" -> 1,
    "SLT" -> 2,
    "SLTU" -> 3,
    "XOR" -> 4,
    "SRL" -> 5,
    "OR" -> 6,
    "AND" -> 7,
    "SUB" -> 8,
    "MUL" -> 9,
    "MULH" -> 10,
    "MULHSU" -> 11,
    "MULHU" -> 12,
    "SRA" -> 13,
    "COPY" -> 15,
    "DIV" -> 16,
    "DIVU" -> 17,
    "REM" -> 18,
    "REMU" -> 19
  )

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

  def assignAll(list: List[Any],output:OutPut) = {
      /* notice that should be order by order */
      output.inst += list(0).asInstanceOf[String]
      output.illegal += list(1).asInstanceOf[Boolean]
      output.users1 += list(2).asInstanceOf[Boolean]
      output.users2 += list(3).asInstanceOf[Boolean]
      output.userd += list(4).asInstanceOf[Boolean]
      output.jump += list(5).asInstanceOf[Boolean]
      output.fencei += list(6).asInstanceOf[Boolean]
      output.compress += list(7).asInstanceOf[Boolean]
      output.branch += list(8).asInstanceOf[Int]
      output.instString += list(9).asInstanceOf[String]
      output.instValue += list(10).asInstanceOf[Long]
      output.rs1 += list(11).asInstanceOf[Int]
      output.rs2 += list(12).asInstanceOf[Int]
      output.rd += list(13).asInstanceOf[Int]
      output.op1 += list(14).asInstanceOf[Int]
      output.op2 += list(15).asInstanceOf[Int]
      output.mask += list(16).asInstanceOf[Int]
      output.alu += list(17).asInstanceOf[Int]
      output.memoryop += list(18).asInstanceOf[Int]
  }

}
