package DefineDevice.GPIO


/* the spinal lib has the gpio device
* the gpio can used as the input Or as the output */

import DefineSim.SIMCFG
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb._
import spinal.lib.bus.amba3.apb.sim.Apb3Driver
import spinal.lib.io.TriStateArray

class GPIOPlay extends PrefixComponent{
  val io = new Bundle{
    val bus = slave(Apb3(32, 32))
    val gpio = master(TriStateArray(32 bits))
  }

  val gpioCtrl = Apb3Gpio(gpioWidth = 32,withReadSync = true)
  io.gpio <> gpioCtrl.io.gpio
  val apbDecoder = Apb3Decoder(
    master = io.bus,
    slaves = List(
      gpioCtrl.io.apb -> (0x10000000,4 KiB),
    )
  )

}

object GPIOPlay {
  def main(args: Array[String]): Unit = {
    val rtl = new RtlConfig().GenRTL(new GPIOPlay())
  }
}


object GPIOPlayTester extends App{
  import spinal.core.sim._

  SIMCFG(compress = true).compile{
    val dut = new GPIOPlay()
    dut
  }.doSimUntilVoid{
    dut =>
      dut.clockDomain.forkStimulus(10)

      /* just write the pin out */
      val driver = Apb3Driver(dut.io.bus,dut.clockDomain)
      driver.write(0x10000008,1)
      driver.write(0x10000004,0x100) /* write this reg to output the data */
      dut.clockDomain.waitSampling()
      println(dut.io.gpio.write.toBigInt)
      println(dut.io.gpio.writeEnable.toBigInt)

      dut.io.gpio.read #= 0x100
      dut.clockDomain.waitSampling()
      println(driver.read(0x10000000))
      simSuccess()
  }
}