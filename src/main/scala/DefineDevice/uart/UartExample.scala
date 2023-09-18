package DefineDevice.uart

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig, addSimPublic}
import DefineUntils.Switch.CrossbarConfig
import spinal.core._
import spinal.core.sim.SimPublic
import spinal.lib._
import spinal.lib.com.uart._
import spinal.lib.bus.amba3.apb._

  /*
  this example show about how to drive the (simple bus / apb bus)  data to the uart
  */

object UartConfig{
    /*
     use the init config can set the clock width
     hwo to set the clock diver width
    */
    val uartCtrlConfig = UartCtrlMemoryMappedConfig(
      uartCtrlConfig = UartCtrlGenerics(
        dataWidthMax = 8,
        clockDividerWidth = 20,
        preSamplingSize = 1,
        samplingSize = 3,
        postSamplingSize = 1
      ),
      initConfig = UartCtrlInitConfig(
        baudrate = 115200,
        dataLength = 7, //7 => 8 bits
        parity = UartParityType.NONE,
        stop = UartStopType.ONE
      ),
      busCanWriteClockDividerConfig = false,
      busCanWriteFrameConfig = false,
      txFifoDepth = 16,
      rxFifoDepth = 16
    )

    def getApb3Config = Apb3Config(
      addressWidth = 5,
      dataWidth = 32,
      selWidth = 1,
      useSlaveError = false
    )

  }

/* drive it with the apb bus*/
class UartExample() extends PrefixComponent{
  val io = new Bundle{
    val uart = master(Uart())
    val apb = slave(new Apb3(UartConfig.getApb3Config))
    val interupt = out Bool()
  }
  val apb3UartCtrl = Apb3UartCtrl(config = UartConfig.uartCtrlConfig)
  apb3UartCtrl.io.apb <> io.apb
  apb3UartCtrl.io.uart <> io.uart
  io.interupt := apb3UartCtrl.io.interrupt
}

object CrossbarExample extends App{
  val rtl = new RtlConfig()
  rtl.setconfig(new UartExample())
}