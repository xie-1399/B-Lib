package DefineDeviceSim.uart

import DefineSim._
import DefineSim.SpinalSim._
import DefineDevice.uart._
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import scala.util.Random


/* the tx will send the data out in the write fifo -> if no cts cmd
*  and monitor the clock tick the time
*  simple send a Hello World*/

object str {
  val code = List(0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x0A)
}

class TxSim extends AnyFunSuite {
  test("uart tx simulation") {
    SIMCFG(gtkFirst = true).compile {
      val gen = UartGen()
      val dut = new Tx(gen)
      addSimPublic(List(dut.clockDivider.tick, dut.stageMachine.state))
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        val tick = dut.clockDivider.tick
        val state = dut.stageMachine.state
        var txd = ""

        def monitor(code: List[Int]) = {
          for (num <- 0 until code.length) {
            dut.io.break #= false
            dut.io.cts #= false
            dut.io.frame.stop #= UartStopType.ONE
            dut.io.frame.parity #= UartParityType.NONE
            dut.io.frame.dataLength #= 7
            dut.io.samplingTick #= true
            dut.io.write.valid #= false
            dut.clockDomain.waitSamplingWhere(tick.toBoolean)
            dut.clockDomain.waitSampling()

            dut.io.samplingTick #= true
            dut.io.write.valid #= true
            dut.io.write.payload #= code(num)
            dut.clockDomain.waitSamplingWhere {
              /* the data state is 2 */
              if (tick.toBoolean && state.toBigInt == 2) {
                txd = txd + dut.io.txd.toBigInt.toString()
              }
              dut.io.write.ready.toBoolean
            }
            val res = Integer.parseInt(txd.reverse, 2)
            assert(code(num) == res)
            if (res.toChar != '\r') print(res.toChar)

            dut.io.write.valid #= false
            dut.io.write.payload #= 0
            dut.clockDomain.waitSamplingWhere {
              txd = ""
              state.toBigInt == 0
            }
          }
        }
        monitor(str.code)
    }
  }

}
