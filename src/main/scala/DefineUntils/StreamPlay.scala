package DefineUntils

import DefineSim.SIMCFG
import DefineSim.SpinalSim.PrefixComponent
import org.scalatest.funsuite.AnyFunSuite
import spinal.core._
import spinal.lib._

/* the Stream contains the valid and ready signal of the datatype
*  it's a very important concept in the spinal lib for translation
* document : https://github.com/SpinalHDL/SpinalHDL/blob/dev/lib/src/main/scala/spinal/lib/Stream.scala
* the simulation is at test */

object StreamPlayer{

  /* this shows about how to control the stream -> important to use it */
  class streamWait extends PrefixComponent{
    val io = new Bundle{
      val raw1 = slave(Stream(Bits(5 bits)))
      val raw2 = slave(Stream(Bits(5 bits)))
      val ready = in Bool()
    }
    /* the wave will show how to delay a stage */
    val m2s = io.raw1.m2sPipe() /* the ready stage will high if no m2s valid happens */
    m2s.ready := io.ready  /* the valid and payload will hold until self.ready */

    val s2m = io.raw2.s2mPipe()
    s2m.ready := io.ready
  }

  class streamChain extends PrefixComponent{
    val io = new Bundle{
      val raw = slave Stream(UInt(10 bits))
      val dataIn = in Bits(10 bits)
      val chain = in Bool()
    }

    val readyChain = RegNext(io.chain)

    val isStall = io.raw.isStall  /* when the request is come but not ready for it */
    val isNew = io.raw.isNew  /* show the stream is first cycle valid */
    val isFree = io.raw.isFree /* no data comes but trans is ready for it */

    val raw1 = Stream(io.raw.payloadType)
    raw1.connectFrom(io.raw) /* raw1 connect with the raw (notice the ready) */

    val raw2 = io.raw.clone()
    raw2.arbitrationFrom(raw1)  /* with ready and valid connect */
    raw2.payload := raw1.payload + 1

    val raw3 = cloneOf(io.raw)
    raw3.translateFrom(raw2)((to,from) => to := from + 1)  /* for the payload data translate */

    val raw4 = raw3.toEvent() /* ignore the payload with the event */

    val raw5 = raw4.translateWith(io.dataIn) /* translate with get a new stream and with new payload */
    /* use the transmuteWith will change the payload type(should be same bits length ) */

    val raw6 = raw5.swapPayload(io.dataIn)
    raw6.payload := 0

    val raw7 = raw6.combStage() /* create a new stream same as the raw6 */

    val raw8 = Stream(raw7.payloadType)
    raw7 >/-> raw8  /* connect the stream with all signal ony cycle late value */

    // val raw9 = raw8.repeat(5) /* raw 8 ready only when raw 9 repeat counter times */

    val raw9 = raw8.clearValidWhen(io.chain)
    raw9.ready := readyChain

  }



  class streamCtrl extends PrefixComponent{
    /* there will be some ways to clone the stream types clone for more*/
    val io = new Bundle{
      val raw = slave Stream(UInt(10 bits))
      val halt = in Bool()
      val continous = in Bool()
      val throwIt = in Bool()
    }

    val streams = StreamFork3(io.raw) /* this is synchronous */

    val useLess = Stream(io.raw.payloadType)
    useLess.setIdle().setBlocked() /* the stream will be use less */

    streams._1.ready := True
    streams._2.ready := True
    streams._3.ready := True

//    val conti = streams._1.continueWhen(io.continous)
//    val halt = streams._2.haltWhen(io.halt)
//    val throwIt = streams._3.throwWhen(io.throwIt)
  }

}
