package DefineMem
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import DefineUntils._
/*
  define some useful memory operation here(adn some comes from lib Mem)
  more details and methods is on : https://github.com/SpinalHDL/SpinalHDL/blob/dev/lib/src/main/scala/spinal/lib/Mem.scala
*/

object MemOperation{

    /* create a new ram */
    def apply[T <: Data](word: HardType[T], depth: Int): Mem[T] = {
      Mem.fill(depth)(word)
    }

   /* if should has initial value about*/
    def apply[T <: Data](word:HardType[T],depth:Int,init:BigInt):Mem[T] = {
      val mem = apply(word,depth)
      mem.initBigInt((0 until mem.wordCount).map(_ => init))
    }
    def apply[T<:Data](word:HardType[T],depth:Int,init:Int):Mem[T] = apply(word,depth,BigInt(init))


    /* simulation about the ram if is sim public*/
    def getSim[T<:Data](mem:Mem[T],address:Long) = {
       mem.getBigInt(address)
    }
    def setSim[T <: Data](mem: Mem[T], address: Long, data:BigInt) = {
      mem.setBigInt(address,data)
    }

    /* if want to create read or write port -> use the api is enough*/


}

