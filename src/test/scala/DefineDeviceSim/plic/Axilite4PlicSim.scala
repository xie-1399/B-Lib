package DefineDeviceSim.plic

import DefineSim.{SIMCFG, SimUntils}
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim.simTime
import DefineDevice.Interrupt.PLic._
import spinal.lib.bus.amba3.apb.Apb3Config
import spinal.core.sim._

class Axilite4PlicSim extends AnyFunSuite{
  test("axi lite4 drive the plic") {
    SIMCFG(gtkFirst = true).compile {
      val dut = new plicDemo()
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        /* Todo write the simulation model */

        /* first set the priority */

    }
  }
}
