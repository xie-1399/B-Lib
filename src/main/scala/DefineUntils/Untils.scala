package DefineUntils

import spinal.lib._
import spinal.core._
import spinal.core.internals.Operator.BitVector

/*
define some some interesting untils find in the lib/utils
*/
object Untils {
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
  def addWithCarry(left: UInt, right: UInt) = {
    AddWithCarry(left, right)
  }

  /*
  Delay until : use the for loop to delay with it, can set with init value and cond
  look the source code can to see how to create lots of regs using for loop
  */
  def delay[T <: Data](that: T, cycleCount: Int, cond: Bool = null, init: T = null.asInstanceOf[T]) = {
    Delay(that, cycleCount, cond, init)
  }

  /*
  history use: delayed with length-1(get all the history list)
  */
  def history[T <: Data](that: T, length: Int, when: Bool = null, init: T = null) = {
    History(that, length, when, init)
  }

  /*
  get the one total number or lots bits of it in the the bools or BitVector
  */
  def getOneNumber(thats: Seq[Bool]): UInt = { //Bitvector can convert to Bools
    CountOne(thats)
  }
  def getOneEachNumber(thats:Seq[Bool]) : Seq[UInt] = {
    CountOneOnEach(thats)
  }
  /* get the condition value from the vec */
  def CountNumber[T<:Data](thats:Vec[T],value:T) = {
    thats.sCount(value)  /* return the UInt value */
  }


  /* Return True if the number of bit set is > x.size / 2 */
  def OverHalfOne(thats:IndexedSeq[Bool]): Bool = {
    MajorityVote(thats)
  }


  /* use the list to assign the Bundle value */
  def assignBundleWithList(bundle:Bundle,seq:Seq[BaseType]) = {
    val flat = bundle.flatten
    flat.zipWithIndex.map {
      case (elem, index) => elem := seq(index)
    }
  }

  /* this shows about how to find in the list whether exist right value */
  def equalWithList[T<:Data](data:T,content:Seq[T]):Bool = {
    val bools = content.map(_ === data)
    getOneNumber(bools) > 0
  }

  /* the Fill building with the same data equals the Repeat */
  def Fill[T <: Data](value : T, times : Int)  = {
    Repeat(value,times)
  }

  /* Cat bits in the Order (in the spinal lib is cat(a,b) is b ## a) */
  object CatInorder {
    def apply(data: Data*): Bits = apply(data.toList.reverse)
    def apply[T <: Data](data: Vec[T]): Bits = data.asBits

    def apply[T <: Data](data: Iterable[T]) = {
      if (data.isEmpty) B(0, 0 bit)
      else data.map(_.asBits).reduce((a,b) => a ## b)
    }
  }

}
