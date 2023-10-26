package DefineDevice.uart

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig, addSimPublic}
import DefineUntils.Switch.CrossbarConfig
import spinal.core._
import spinal.core.sim.SimPublic
import spinal.lib._
import spinal.lib.com.uart._
import spinal.lib.bus.amba3.apb._

  /*
  this example show about the uart control usage
  the more clear examples is on the Apb3Decoder Play (show how to use the apb drive the uart control)
  */

class UartCtrlExample() extends Component{
  val io = new Bundle{
    val uart = master(Uart())
    val switchs = in Bits(8 bits)
    val leds = out Bits(8 bits)
  }

  val uartCtrl: UartCtrl = UartCtrl(
    config = UartCtrlInitConfig(
      baudrate = 921600,
      dataLength = 7,  // 8 bits
      parity = UartParityType.NONE,
      stop = UartStopType.ONE
    )
  )
  uartCtrl.io.uart <> io.uart

  //Assign io.led with a register loaded each time a byte is received
  io.leds := uartCtrl.io.read.toFlow.toReg()

  //Write the value of switch on the uart each 4000 cycles
  val write = Stream(Bits(8 bits))
  write.valid := CounterFreeRun(2000).willOverflow
  write.payload := io.switchs
  write >-> uartCtrl.io.write
}

object UartExample extends App{
  val rtl = new RtlConfig().GenRTL(top = new UartCtrlExample())
}