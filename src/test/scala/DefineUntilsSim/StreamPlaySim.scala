package DefineUntilsSim

import DefineSim._
import DefineUntils._
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import spinal.lib.sim.{StreamDriver, StreamMonitor, StreamReadyRandomizer}
import scala.util.Random
/* more Stream function and until can be tested here */

class StreamPlaySim extends AnyFunSuite{

  test("wait"){
    SIMCFG(gtkFirst = true).compile{
      val dut = new StreamPlayer.streamWait()
      dut
    }.doSim{
      dut =>
        var thread1_nums = 0
        var thread2_nums = 0
        dut.clockDomain.forkStimulus(10)
        dut.clockDomain.onSamplings{
          dut.io.ready.randomize()
        }


        /* the driver will get it with random signal and the ready should not always down*/
          val thread1 = fork{
            StreamDriver(dut.io.raw1,dut.clockDomain){
              payload =>
                dut.io.raw1.payload.randomize()
                thread1_nums += 1
                true
            }
          }
          val thread2 = fork{
            StreamDriver(dut.io.raw2, dut.clockDomain) {
              payload =>
                dut.io.raw2.payload.randomize()
                thread2_nums += 1
                true
            }
          }
        dut.clockDomain.waitActiveEdgeWhere{
          if(thread1_nums + thread2_nums > 20) println(s"thread_1: ${thread1_nums}\t thread_2: ${thread2_nums}")
          thread1_nums + thread2_nums > 20
        }
    }
  }


  test("chain"){
    SIMCFG(gtkFirst = true).compile{
      val dut = new StreamPlayer.streamChain()
      dut
    }.doSim{
      dut =>
        var fire = 0
        dut.clockDomain.forkStimulus(10)
        /* random stream of the slave */
        StreamDriver(dut.io.raw,dut.clockDomain){ payload =>
          dut.io.chain #= Random.nextInt(10) > 5
          dut.io.dataIn.randomize()
          payload.randomize()
          true
        }

        StreamMonitor(dut.io.raw,dut.clockDomain){
          payload =>
            fire += 1
        }

        dut.clockDomain.waitActiveEdgeWhere(fire == 10)  /* the stream will fire 10 times*/
    }
  }

  test("ctrl"){
    SIMCFG(compress = true).compile {
      val dut = new StreamPlayer.streamCtrl()
      dut
    }.doSim {
      dut =>
        var fire = 0
        dut.clockDomain.forkStimulus(10)
        /* random stream of the slave */
        dut.clockDomain.onSamplings{
          dut.io.halt.randomize()
          dut.io.continous.randomize()
          dut.io.throwIt.randomize()
          dut.io.done1.randomize()
          dut.io.done2.randomize()
          dut.io.done3.randomize()
        }
        /* the driver will drive the data only the fire happens (if not ready will block it)*/
        StreamDriver(dut.io.raw, dut.clockDomain) { payload =>
          payload.randomize()
          true
        }

        StreamMonitor(dut.io.raw, dut.clockDomain) {
          payload =>
            fire += 1
        }

        dut.clockDomain.waitActiveEdgeWhere(fire == 10) /* the stream will fire 10 times*/
    }
  }

}

/* this is a way to save the simulation report function */
object StreamPlaySim{
  def main(args: Array[String]): Unit = {
    val report = Logger.SimReport(new StreamPlaySim(),testName = Some("wait"),saved = false)
  }
}
