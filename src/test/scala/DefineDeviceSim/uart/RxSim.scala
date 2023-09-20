package DefineDeviceSim.uart

import DefineDevice.uart.source._
import DefineSim.SIMCFG
import DefineSim.SpinalSim.{addSimPublic, onlySample}
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._

import scala.util.Random
/*
 throw the Rx sim more understand history and MajorityVote
*/

class RxSim extends AnyFunSuite {
  test("uart rx simulation") {
    SIMCFG(gtkFirst = true).compile {
      val gen = UartGen()
      val dut = new Rx(gen)
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        def monitor() = {
          dut.io.break #= false
          dut.io.frame.stop #= UartStopType.ONE
          dut.io.frame.parity #= UartParityType.NONE
          dut.io.frame.dataLength #= 7
          dut.io.samplingTick #= true
          dut.io.rxd #= Random.nextInt(10) > 5
        }

        onlySample(dut.clockDomain,operation = monitor)
    }
  }
}
