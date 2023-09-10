package DefineUntils
import spinal.lib._
import spinal.core._
  /*
  define some some interesting untils find in the lib/utils
  */
object Others {
  /*
  let the bitvector(Bits/UInt/SInt) bits reversed
  */
  def reverse(bitVector: BitVector) = {
    val value = Reverse(bitVector)
    value
  }

  /*
  can help with the carry
  */
  def addWithCarry(left:UInt,right:UInt)= {
    AddWithCarry(left,right)
  }

  /*
  Delay until : use the for loop to delay with it, can set with init value and cond
  look the source code can to see how to create lots of regs using for loop
  */
  def delay[T<:Data](that:T,cycleCount:Int,cond:Bool = null,init:T = null.asInstanceOf[T]) = {
    Delay(that,cycleCount,cond,init)
  }

  /*
  history use: delayed with length-1(get all the history list)
  */
  def history[T<:Data](that: T, length: Int, when: Bool = null, init: T = null) = {
    History(that,length,when,init)
  }
}
