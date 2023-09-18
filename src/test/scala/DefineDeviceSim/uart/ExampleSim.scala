package DefineDeviceSim.uart

import DefineBus.Apb3.APBOperation
import DefineDevice.uart._
import DefineSim.SIMCFG
import DefineSim.SpinalSim.{RtlConfig, addSimPublic}
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import spinal.lib.com.uart.sim._

class ExampleSim extends AnyFunSuite {
  test("uart tx simulation") {
    SIMCFG(gtkFirst = true).compile {
      val rtl = new RtlConfig()
      val dut = rtl.setconfig(new UartExample())
      dut
    }.doSimUntilVoid {
      dut =>
        dut.clockDomain.forkStimulus(10)
        val uartBaudRate = 115200
        val uartBaudPeriod = (1e12 / uartBaudRate).toLong

        val driver = APBOperation.sim(dut.io.apb,dut.clockDomain)
        for(idx <- 0 until str.code.length){
          driver.write(idx,str.code(idx))
        }

        val uartTx = UartDecoder(
          uartPin = dut.io.uart.txd,
          baudPeriod = uartBaudPeriod
        )


    }
  }

}
