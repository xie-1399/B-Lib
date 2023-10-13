package DefineDevice.Interrupt

/* read the source code of clint and know the purpose and use of it
* CLINT is responsible for maintaining memory mapped control and status registers which are associated with the software and timer interrupts.
* material: https://chromitem-soc.readthedocs.io/en/latest/clint.html is here
* */


import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb._
import spinal.lib.bus.amba4.axilite._
import spinal.lib.bus.wishbone._
import spinal.lib.misc._

/* the clint is drived by some buses,so provide a interface and know how to use it is enough */

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



