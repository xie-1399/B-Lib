package DefineDevice.uart

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import spinal.lib.com.uart._
import spinal.lib.bus.amba3.apb._

  /*
  this example show about how to drive the (simple bus / apb bus)  data to the uart
  */

object uartConfig{

}


/* drive it with the apb bus*/
class UartExample(val apbConfig:Apb3Config) extends PrefixComponent{
  val io = new Bundle{
    val uart = Uart()
    val apb = master(new Apb3(apbConfig))
    val interrupt = out Bool()
  }


  // val apb3UartCtrl = Apb3UartCtrl()

}


class SMBUartExample() extends PrefixComponent {
  /*
  drive the simple bus with the uart
  */
}