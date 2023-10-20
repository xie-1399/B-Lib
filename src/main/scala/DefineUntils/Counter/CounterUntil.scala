package DefineUntils.Counter

import spinal.core._
import spinal.lib._

object CounterUntil {

  /* two define types counter*/
  def cycleCounter(start:BigInt,end:BigInt) = DefineCounter(start,end,true)
  def keepCounter(start:BigInt,end:BigInt) = DefineCounter(start,end,false)
  def toFlow(defineCounter: DefineCounter): Flow[UInt] = defineCounter.toFlow()
  def init(defineCounter: DefineCounter,init:BigInt) = defineCounter.init(init)

  /* for counters with init value */
  def counterInit(Counters:Seq[Counter],init:BigInt) = {
    for(counter <- Counters){
      counter.init(init)
    }

  }


}
