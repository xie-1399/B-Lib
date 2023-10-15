 package DefineUntils.ProMux

import spinal.core._
import spinal.lib._
 /*
 useful skills to operator the one hot vector and crest show the max and min
 more likely the lib operation at  https://github.com/SpinalHDL/SpinalHDL/blob/dev/lib/src/main/scala/spinal/lib/Utils.scala
 */


object OneHotOperation {
  /*
  UInt to one hot with mapping value
  */
  //U(3,2bits) -> B(1000)
  def UInt2Oh(value:UInt):Bits = UIntToOh(value)
  def UInt2OhBools(value: UInt): Vec[Bool] = UIntToOh(value).asBools
  //U(1) Seq(1,2,3) -> B(001)
  def UIntMapping(value:UInt,mapping:Seq[Int]) = UIntToOh(value,mapping)

  /*
  to let the one hot value -> UInt (with mapping)
  */
  def BoolsOh2UInt(bools: Seq[Bool]): UInt = OHToUInt(bools)
  def BitVectorOh2UInt(bitVector: BitVector) = OHToUInt(bitVector) //just set the bitvector.asBools()
  def MappingBoolsOh2UInt(oh:Seq[Bool],mapping:Seq[Int]):UInt = OHToUInt(oh,mapping)
  def MappingBitVectorOh2UInt(bitVector: BitVector,mapping:Seq[Int]) : UInt = OHToUInt(bitVector,mapping)

  //B(011) -> B(001)
  def wordLSBOh(value:Data) = OHMasking.first(value)
  def BoolswordLSBOh(bools:Vec[Bool]) = OHMasking.firstV2(bools)
  def wordLSBOhUp(value:Data) = OHMasking.firstV2(value)

  def BoolswordMSBOh(bools:Vec[Bool]) = OHMasking.lastV2(bools)
  def wordMSBOh(value:Data) = OHMasking.last(value)
  def wordMSBOhUp(value:Data) = OHMasking.lastV2(value)

  /*
  from oneHot vec to get mapping value
  */
  def BoolsOhMapping[T <: Data](oneHot:collection.IndexedSeq[Bool],inputs: Vec[T]):T = {
    assert(oneHot.size == inputs.size)
    oneHot.size match {
      case 2 => oneHot(0) ? inputs(0) | inputs(1)
      case _ => inputs(BoolsOh2UInt(oneHot))
    }
  }
  def BitVectorOhMapping[T<:Data](oneHot:BitVector,inputs:Vec[T]):T = {
    BoolsOhMapping(oneHot = oneHot.asBools,inputs)
  }
}