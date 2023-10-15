package DefineUntils.Counter

import spinal.core._
import spinal.lib._


 /*
  this are very same in the counter lib but will overflow to the start number
 */

class DefineCounter(val start: BigInt, val end: BigInt, val Loop: Boolean = true) extends ImplicitArea[UInt] {
  require(start <= end)
  val willIncrement = False.allowOverride
  val willClear = False.allowOverride
  val Cycle = if(Loop) U(start) else U(end)
  def clear(): Unit = willClear := True

  def increment(): Unit = willIncrement := True

  val valueNext = UInt(log2Up(end + 1) bit)
  val value = RegNext(valueNext) init (start)
  val willOverflowIfInc = value === end
  val willOverflow = willOverflowIfInc && willIncrement

  if (isPow2(end + 1) && start == 0) { //Check if using overflow follow the spec
    valueNext := (value + U(willIncrement)).resized
  }
  else {
    when(willOverflow) {
      valueNext := Cycle.resized
    } otherwise {
      valueNext := (value + U(willIncrement)).resized
    }
  }
  when(willClear) {
    valueNext := start
  }

  willOverflowIfInc.allowPruning()
  willOverflow.allowPruning()

  override def implicitValue: UInt = this.value

  /**
   * Convert this stream to a flow. It will send each value only once. It is "start inclusive, end exclusive".
   * This means that the current value will only be sent if the counter increments.
   */
  def toFlow(): Flow[UInt] = {
    val flow = Flow(value)
    flow.payload := value
    flow.valid := willIncrement
    flow
  }

  def init(initValue: BigInt): this.type = {
    value.removeInitAssignments()
    value.init(initValue)
    this
  }
}


object DefineCounter{

  def apply(start:BigInt,end:BigInt,Loop:Boolean): DefineCounter = new DefineCounter(start, end, Loop)

}
