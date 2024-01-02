package DefineDevice.GPIO

/* here set my gpio module with the gpi in and gpo out */
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb._

/*
* gpioRead -> 0x00 Read only register to read the physical pin values
* gpioWrite -> 0x04 Read -Write register to access the output values
*
* know more about the GPIO (the port is bit by bit )
*/

case class MyApb3Gpio(gpioWidth: Int, withReadSync : Boolean,readBufferLength : Int = 2) extends Component {

  val io = new Bundle {
    val apb  = slave(Apb3(6,32))
    val gpi = in Bits (gpioWidth bits)
    val gpo = out Bits (gpioWidth bits)
    val interrupt = out Bool()
    val value = out Bits(gpioWidth bits)
  }

  io.value := (if(withReadSync) BufferCC(io.gpi) else io.gpi)

  val ctrl = Apb3SlaveFactory(io.apb)
  ctrl.read(io.value, 0)
  ctrl.driveAndRead(io.gpo, 4)
  val syncronized = Delay(io.gpi, readBufferLength)
  val last = RegNext(syncronized)

  val interrupt = new Area {
    val enable = new Area {val high, low, rise, fall = Bool()}

    ctrl.driveAndRead(enable.rise,0x20) init(False)
    ctrl.driveAndRead(enable.fall,0x24) init(False)
    ctrl.driveAndRead(enable.high,0x28) init(False)
    ctrl.driveAndRead(enable.low,0x2C) init(False)

    val valid = ((enable.high & syncronized(0))
      | (enable.low & ~syncronized(0))
      | (enable.rise & (syncronized(0) & ~last(0)))
      | (enable.fall & (~syncronized(0) & last(0))))

    io.interrupt := valid
  }
}
