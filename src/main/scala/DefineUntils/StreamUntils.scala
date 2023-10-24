package DefineUntils

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._

/* the Stream contains the valid and ready signal of the datatype
*  it's a very important concept in the spinal lib for translation
* document : https://github.com/SpinalHDL/SpinalHDL/blob/dev/lib/src/main/scala/spinal/lib/Stream.scala */

class StreamUntils extends PrefixComponent{
  /* there are so many control functions about the Stream Control */
  val io = new Bundle{
    val raw = slave Stream(UInt(32 bits))
  }

  /* create a new Stream with the raw */
  val raw1 = io.raw.clone()
  io.raw >/-> raw1 /* all signals are one cycle late */

  /* to the Event ( seems like no payload data ) / and connect the fire signal */
  val event = io.raw.toEvent()

  val isStall = io.raw.isStall /* valid and ! ready*/

  val rawCarryData = io.raw.asDataStream

  /* connect the Stream */



}
