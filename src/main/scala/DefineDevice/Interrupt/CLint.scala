package DefineDevice.Interrupt

/* read the source code of clint and know the purpose and use of it
* CLINT is responsible for maintaining memory mapped control and status registers which are associated with the software and timer interrupts.
* material: https://chromitem-soc.readthedocs.io/en/latest/clint.html is here
* */


import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import spinal.lib.bus.misc._
import spinal.lib.bus.amba3.apb._
import spinal.lib.bus.amba4.axilite._
import spinal.lib.bus.wishbone._
import spinal.lib.misc._

/* the clint is drived by some buses,so provide a interface and know how to use it is enough */
object ClintSource{
  /* add clint source code here */
  case class Clint(hartCount: Int) extends Area {
    val stop = False
    val time = Reg(UInt(64 bits)) init (0)
    when(!stop) {
      time := time + 1
    }

    val harts = for (hartId <- 0 until hartCount) yield new Area {
      val cmp = Reg(UInt(64 bits))
      val timerInterrupt = RegNext(time >= cmp)
      val softwareInterrupt = RegInit(False)
    }

    /* drive the clint reg with buses  */
    def driveFrom(bus: BusSlaveFactory, bufferTime: Boolean = false) = new Area {
      val IPI_ADDR = 0x0000 /* software interrupt base address */
      val CMP_ADDR = 0x4000 /* compare time reg base address */
      val TIME_ADDR = 0xBFF8 /* read the time in the clint */

      bufferTime match {
        case false => bus.readMultiWord(time, TIME_ADDR)
        case true => new Composite(this) {
          assert(bus.busDataWidth == 32)

          val timeMsb = RegNextWhen(time(63 downto 32), bus.isReading(TIME_ADDR))
          bus.read(time(31 downto 0), TIME_ADDR)
          bus.read(timeMsb, TIME_ADDR + 4)
        }
      }

      val hartsMapping = for (hartId <- 0 until hartCount) yield new Area {
        bus.writeMultiWord(harts(hartId).cmp, CMP_ADDR + 8 * hartId)
        bus.readAndWrite(harts(hartId).softwareInterrupt, IPI_ADDR + 4 * hartId, bitOffset = 0)
      }
    }
  }
}

object CLint{

  def create(bus:Bundle,hartCount:Int = 1) = {
    /* more bus like tilelink need to declare*/
    val clint = bus match {
      case Apb3(config) => Apb3Clint(hartCount) /* the slave bus is (16,32) */
      case Wishbone(config) => WishboneClint(hartCount)
      case AxiLite4(config) => AxiLite4Clint(hartCount)
      case _ => 
    }
    clint
  }

  /* the simulation is on the clint part */
  class ApbDriverClint(c:Apb3Config) extends PrefixComponent{
    /* if the apb bus width is not match  */
    val io = new Bundle{
      val bus = slave(Apb3(config = c))
    }
    val clint = create(io.bus).asInstanceOf[Apb3Clint] /* one hart */
    io.bus >> clint.io.bus /* the address will resized by the connect */
  }

}



