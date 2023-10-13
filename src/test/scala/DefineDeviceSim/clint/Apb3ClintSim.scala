package DefineDeviceSim.clint

import DefineBus.Apb3.APBOperation
import org.scalatest.funsuite.AnyFunSuite
import DefineSim._
import DefineDevice.Interrupt.CLint._
import spinal.lib.bus.amba3.apb.Apb3Config
import spinal.core.sim._
/* use the the apb3 to drive the */

class Apb3ClintSim extends AnyFunSuite{
  test("apb3 drive the clint") {
    SIMCFG(gtkFirst = true).compile {
      val dut = new ApbDriverClint(Apb3Config(32,32))
      dut.clint.io.simPublic()
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)

        val driver = APBOperation.sim(dut.io.bus,dut.clockDomain)

        /* the cmp width is 64 */
        driver.write(0x4000,0x0064)
        driver.write(0x4004,0x0000)
        dut.clockDomain.waitSamplingWhere(dut.clint.io.timerInterrupt.toBigInt == 1)
        SimUntils.logwithCycle(simTime().toString,s"time:${dut.clint.io.time.toBigInt}")

        /* in that case you can read from the address */
        val timer = driver.read(0xBFF8)
        SimUntils.logwithCycle(simTime().toString,s"timer:${timer}")
        SimUntils.logwithCycle(simTime().toString,s"timeOut:${dut.clint.io.time.toBigInt}")

        /* software Interrupt out*/
        driver.write(0x0000,0x1001) /* the lower bit is 1 -> will touch the software interrupt */
        dut.clockDomain.waitSamplingWhere(dut.clint.io.softwareInterrupt.toBigInt === 1)
    }
  }
}
