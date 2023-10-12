package DefineMemSim

import DefineMem._
import DefineSim.SIMCFG
import DefineSim.SpinalSim.addSimPublic
import org.scalatest.funsuite.AnyFunSuite
import spinal.core._
import spinal.core.sim._
import scala.collection.mutable
/* the fifo component should be tested very carefully */


class FIFOSim extends AnyFunSuite {
  test("FIFO simple component") {
    SIMCFG(gtkFirst = true).compile {
      val dut = FIFO(Bits(10 bits),32)
      dut.empty.simPublic()
      dut
    }.doSimUntilVoid {
      dut =>
        dut.clockDomain.forkStimulus(10)

        val queueModel = mutable.Queue[Long]()
        SimTimeout(1000000 * 10)

        // Push data randomly, and fill the queueModel with pushed transactions.
        val pushThread = fork {
          dut.io.enqueue.valid #= false
          while (true) {
            dut.io.enqueue.valid.randomize()
            dut.io.enqueue.payload.randomize()
            dut.clockDomain.waitSampling()
            if (dut.io.enqueue.valid.toBoolean && dut.io.enqueue.ready.toBoolean) {
              queueModel.enqueue(dut.io.enqueue.payload.toLong)
            }
          }
        }

        // Pop data randomly, and check that it match with the queueModel.
        val popThread = fork {
          dut.io.dequeue.ready #= true
          for (i <- 0 until 100000) {
            dut.io.dequeue.ready.randomize()
            dut.clockDomain.waitSampling()
            if (dut.io.dequeue.valid.toBoolean && dut.io.dequeue.ready.toBoolean) {
              assert(dut.io.dequeue.payload.toLong == queueModel.dequeue())
            }
          }
          simSuccess()
        }


    }
  }

}
