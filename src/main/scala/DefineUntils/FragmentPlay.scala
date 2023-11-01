package DefineUntils

import DefineMem.MemOperation
import DefineSim.SIMCFG
import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import spinal.lib.sim.{StreamDriver, StreamMonitor}

import scala.collection.mutable
import scala.util._
import DefineSim.SimUntils._
/*
 * the fragment  will  transmit big thing by multiple small fragments
 * the document : https://spinalhdl.github.io/SpinalDoc-RTD/v1.8.0/SpinalHDL/Libraries/fragment.html#fragment */

case class fragmentFormat(addrWidth:Int = 3,dataWidth:Int = 8,length:Int = 4){
  def transLen = length * dataWidth
}

case class writeFragment(f:fragmentFormat) extends Bundle{
  val addr = UInt(f.addrWidth bits)
  val data = Bits(f.dataWidth bits)
}

/* simulation success */

class FragmentPlay(f:fragmentFormat) extends PrefixComponent{

  val io = new Bundle{
    val trans = slave Stream(Fragment(writeFragment(f))) /* translate each time */
  }
  val mem = MemOperation(Bits(f.transLen bits),Math.pow(2,f.addrWidth).toInt)

  io.trans.ready := True
  val addr = RegNextWhen(io.trans.payload.addr,io.trans.first)
  val bitsCounter = Counter(f.length)
  when(io.trans.valid && !io.trans.first){
    bitsCounter.increment()
  }
  when(io.trans.last){
    bitsCounter.clear()
  }
  val history = History(io.trans.payload.data,4,io.trans.valid && !io.trans.first)
  mem.write(addr,history.asBits,bitsCounter.willOverflowIfInc)
}

object FragmentPlay extends App{
  import spinal.core.sim._
  SIMCFG(compress = true).compile{
    val dut = new FragmentPlay(fragmentFormat())
    dut.mem.simPublic()
    dut
  }.doSim{
    dut =>
      dut.clockDomain.forkStimulus(10)
      /* simulation about it */

      val queue = mutable.Queue[Byte]()
      dut.io.trans.valid #= false
      dut.io.trans.last #= false
      dut.clockDomain.waitSampling()

      def monitor(iteration:Int = 100) = {
        for(idx <- 0 until iteration){
          dut.io.trans.valid #= true
          dut.io.trans.last #= false
          val address = Random.nextInt(7)
          dut.io.trans.addr #= address
          dut.io.trans.data.randomize()
          dut.clockDomain.waitSampling()
          for (idx <- 0 until 4) {
            val last = if (idx == 3) true else false
            val data = Random.nextInt(10) + 1
            dut.io.trans.valid #= true
            dut.io.trans.last #= last
            dut.io.trans.addr.randomize()
            dut.io.trans.data #= data
            queue.enqueue(data.toByte)
            dut.clockDomain.waitSampling()
          }
          dut.io.trans.valid #= false
          dut.io.trans.last #= false
          dut.clockDomain.waitSampling()
          println("dut:" + dut.mem.getBigInt(address.toLong).toByteArray.mkString(","))
          println("ref:" + queue.mkString(","))
          assert(compare(dut.mem.getBigInt(address.toLong).toByteArray,queue.toArray))
          queue.clear()
        }

      }
      monitor(1000)

  }

}