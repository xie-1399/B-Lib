package DefineUntils

import DefineMem.MemOperation
import DefineSim.SIMCFG
import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import spinal.lib.sim.{StreamDriver, StreamMonitor}

import scala.collection.mutable
import scala.util.Random

/*
 * the fragment  will  transmit big thing by multiple small fragments
 * the document : https://spinalhdl.github.io/SpinalDoc-RTD/v1.8.0/SpinalHDL/Libraries/fragment.html#fragment */

case class writeFragment() extends Bundle{
  val addr = UInt(3 bits)
  val data = Bits(8 bits)
}

/* Todo each the fragment coming -> it also work*/

class FragmentPlay extends PrefixComponent{

  val io = new Bundle{
    val trans = slave Stream(Fragment(writeFragment()))
  }
  val mem = MemOperation(Bits(32 bits),8)

  io.trans.ready := True
  val addr = RegNextWhen(io.trans.payload.addr,io.trans.first)
  val bitsCounter = Counter(0,4)
  when(io.trans.valid && !io.trans.first){
    bitsCounter.increment()
  }
  val history = History(io.trans.payload.data,4,io.trans.valid && !io.trans.first,init = B"0".resized)
  val counter = Counter(0,8)

  mem.write(addr,history.asBits,bitsCounter.willOverflowIfInc)
}

object FragmentPlay extends App{
  import spinal.core.sim._
  SIMCFG(compress = true).compile{
    val dut = new FragmentPlay()
    dut.mem.simPublic()
    dut
  }.doSim{
    dut =>
      dut.clockDomain.forkStimulus(10)
      /* simulation about it */

      val queue = mutable.Queue[BigInt]()
      dut.io.trans.valid #= false
      dut.io.trans.last #= false
      dut.clockDomain.waitSampling()

      def monitor() = {
        dut.io.trans.valid #= true
        dut.io.trans.last #= false
        val address = Random.nextInt(7)
        dut.io.trans.addr #= address
        dut.io.trans.data.randomize()
        dut.clockDomain.waitSampling()
        for(idx <- 0 until 4){
          val last = if(idx == 3) true else false
          val data = Random.nextInt(10)
          dut.io.trans.valid #= true
          dut.io.trans.last #= last
          dut.io.trans.addr.randomize()
          dut.io.trans.data #= data
          queue.enqueue(data)
          dut.clockDomain.waitSampling()
        }
        dut.io.trans.valid #= false
        dut.io.trans.last #= false
        dut.clockDomain.waitSampling(3)

        println("dut:" + dut.mem.getBigInt(address.toLong).toLong.toHexString)
        println("ref:" + queue.mkString(","))
      }
      monitor()

  }

}