package DefineUntils

import spinal.lib._
import spinal.core._
import DefineUntils.DefineCounter
//all kinds of counter in the spinal lib

object CounterUntil {

  /*
  two define types counter
  */
  def cycleCounter(start:BigInt,end:BigInt) = DefineCounter(start,end,true)
  def keepCounter(start:BigInt,end:BigInt) = DefineCounter(start,end,false)
  def toFlow(defineCounter: DefineCounter): Flow[UInt] = defineCounter.toFlow()
  def init(defineCounter: DefineCounter,init:BigInt) = defineCounter.init(init)



}
