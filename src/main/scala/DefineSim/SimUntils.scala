package DefineSim

import scala.collection.mutable._
import scala.util.Random
import spinal.core._
import spinal.core.sim._
import Logger._
import scala.sys.process._

object SimUntils {

  def GenRandomList(base:Int,offset:Int,num:Int,log:Boolean = false,prefix:String = null):ArrayBuffer[BigInt] = {
    val res = ArrayBuffer[BigInt]()
    for(idx <- 0 until num){
      val value = Random.nextInt(offset) + base
      res += BigInt(value)
    }
    if(log){print(prefix); res.foreach(ele => print(s"$ele" + "\t"));print("\n")}
    res
  }

  /* from the list get the random value */
  def getOneRandomValue[T](list: List[T], num: Int = 1) = {
    require(num < list.length)
    val random = Random.nextInt(list.length)
    val value = list(random)
    value
  }

  def logwithCycle(simTime:String,content:String): Unit = {
    println(s"[${simTime}]\t" + content)
  }

  /* compare the two array content( whether is the same ) */
  def compare[T](ref: Array[T], dut: Array[T]): Boolean = {
    ref.deep == dut.deep
  }


  /* add trans function to simulate the spinal enum */
  def getEnumEncodingValue[T <: SpinalEnum](sim:SpinalEnumCraft[T]) = {
    sim.getEncoding.getValue(sim.toEnum) /* the really encoding Big Int value in the simulation */
  }
  
  /* convert value type to BigInt for simulation purpose */
  class toBigInt {
    def apply(value: Int) = {
      BigInt(value)
    }

    def apply(value: Long) = {
      BigInt(value)
    }

    def apply(value: Byte) = {
      BigInt(value)
    }
  }

  /* the mask function when writing happens
  * getMaskValue(255,16,1,4) -> will get 15 */
  def getMaskValue(value:BigInt,dataWidth:Int,mask:Long,maskWidth:Int) = {
    val real = HexStringWithWidth(value.toLong.toBinaryString,dataWidth)
    val maskBits = HexStringWithWidth(mask.toBinaryString,maskWidth)
    var realBin = ""

    val stride = dataWidth / maskWidth
    val buffer  = ArrayBuffer[String]()
    for(idx <- 0 until maskWidth){
      if(maskBits(idx) == '1') buffer += real.substring(idx * stride,(idx + 1) * stride)
      else buffer += HexStringWithWidth("0",stride)
    }
    /* println(buffer.mkString(",")) */
    for(idx <- 0 until buffer.length){
      realBin += buffer(idx)
    }
    BigInt(realBin,2)
  }

  /* get the bits value from the range and should be include left and right range */
  def getBitsValueInRange(data:BigInt,range: Range,dataWidth:Int,little:Boolean = true) = {
    val binary = HexStringWithWidth(data.toLong.toBinaryString,dataWidth)
    val start = range.start
    val end = range.end
    val lstart = dataWidth - 1 - end
    val lend = dataWidth - 1 - start
    val bitsBin = if(little) binary.substring(lstart,lend + 1) else binary.substring(start , end + 1)
    BigInt(bitsBin,2)
  }

  /* more way to use the substring */
  def subString(bin: String, start: Int, end: Int, left: Boolean = false): String = {
    require(end >= start)
    if (left) bin.substring(start, end)
    else {
      val len = bin.length
      bin.substring(len - end, len - start)
    }
  }

  /* run the command in the bash */
  def commandRun(command:String,logIt:Boolean = false) = {
    val out = command.!! /* run the command */
    if(logIt) println(out)
  }

}

object VecSim{
  /* use the seq to sim the vec value */
  def VecUInt(values: Seq[BigInt], vec: Vec[UInt]) = {
    require(values.length == vec.length)
    for (idx <- 0 until values.length) {
      vec(idx) #= values(idx)
    }
  }

  def VecSInt(values: Seq[BigInt], vec: Vec[SInt],sign:Boolean) = {
    require(values.length == vec.length)
    require(sign)
    for (idx <- 0 until values.length) {
      vec(idx) #= values(idx)
    }
  }

  def VecBits(values: Seq[BigInt], vec: Vec[Bits]) = {
    require(values.length == vec.length)
    for (idx <- 0 until values.length) {
      vec(idx) #= values(idx)
    }
  }

  def VecBool(values: Seq[Boolean], vec: Vec[Bool]) = {
    require(values.length == vec.length)
    for (idx <- 0 until values.length) {
      vec(idx) #= values(idx)
    }
  }

  /* print format of the vector bits */
  def logout[T<:BitVector](values:Vec[T],seperate:Int) = {
    for(idx <- 0 until values.length){
      print(values(idx).toBigInt.toString() + "\t")
      if((idx + 1) % seperate == 0){
        println()
      }
    }
  }
}