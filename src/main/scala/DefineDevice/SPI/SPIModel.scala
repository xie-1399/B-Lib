package DefineDevice.SPI

import DefineSim.SIMCFG
import DefineSim.SpinalSim._
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb._
import spinal.lib.bus.amba3.apb.sim.Apb3Driver
import spinal.lib.com.spi._


/*
  checking the the spi work way in the spinal libs
  using the sd card models test about it
 */


class SPIModel extends PrefixComponent{

  val io = new Bundle{
    val bus = slave (Apb3(32,32))
    val spi = master(SpiMaster())
  }

  val spiConfig = SpiMasterCtrlMemoryMappedConfig(
    ctrlGenerics = SpiMasterCtrlGenerics(
      ssWidth = 1,
      timerWidth = 12,
      dataWidth = 8
    ),
    cmdFifoDepth = 32,
    rspFifoDepth = 32
  )

  val ctrl = Apb3SpiMasterCtrl(spiConfig)
  val apbDecoder = Apb3Decoder(
    master = io.bus,
    slaves = List(
      ctrl.io.apb -> (0x61000000, 32 KiB)
    )
  )
  io.spi <> ctrl.io.spi
}


object SPIModel extends App{

  import spinal.core.sim._
  val rtl = new RtlConfig().GenRTL(new SPIModel())

  SIMCFG().compile{
    val dut = new SPIModel()
    dut
  }.doSimUntilVoid{
    dut =>
      dut.clockDomain.forkStimulus(10)

      val driver = Apb3Driver(dut.io.bus,dut.clockDomain)

      /* send the bytes out */
      driver.write(0x61000008,0x00000000)
      driver.write(0x61000000,0x11000000) /* enable the ss line 0 */
      driver.write(0x61000000,0x00000010)
      dut.clockDomain.waitSampling(8)

      driver.write(0x61000000,0x01000020)

      /* read the data in and read the registers */
      dut.io.spi.miso.randomize()
      println(driver.read(0x61000000))

      simSuccess()
  }

}