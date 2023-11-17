package DefineSim

import scala.collection.mutable._
import scala.util.Random
import spinal.core._
import spinal.core.internals.Operator.BitVector
import spinal.sim._
import spinal.lib.sim._
import spinal.core.sim._
import Logger._

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

  /* the mask function when writing happens */
  def getMaskValue(value:BigInt,dataWidth:Int,mask:Long,maskWidth:Int) = {
    val real = HexStringWithWidth(value.toLong.toBinaryString,dataWidth)
    val maskBits = mask.toBinaryString
    var realBin = ""

    val stride = dataWidth / maskWidth
    val buffer  = ArrayBuffer[String]()
    for(idx <- 0 until maskWidth){
      if(maskBits(idx) == '1') buffer += real.substring(idx * stride,(idx + 1) * stride)
      else buffer += HexStringWithWidth("0",stride)
    }
    println(buffer.mkString(","))
    for(idx <- 0 until buffer.length){
      realBin += buffer(idx)
    }
    println(realBin)

  }
}
object test extends App{
  SimUntils.getMaskValue(15,4,2,2)
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