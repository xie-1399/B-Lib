package DefineSim

import scala.collection.mutable._
import scala.util.Random
import spinal.core._
import spinal.core.internals.Operator.BitVector
import spinal.sim._
import spinal.lib.sim._
import spinal.core.sim._
/*
here set some simulation function until for the spinal sim
*/

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
