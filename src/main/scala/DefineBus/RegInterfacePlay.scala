package DefineBus

import DefineBus.Apb3.APBOperation
import DefineSim.SIMCFG
import DefineSim.SpinalSim.PrefixComponent
import spinal.lib._
import spinal.core._
import spinal.lib.bus.amba3.apb.Apb3Config
import spinal.lib.bus.regif.{Apb3BusInterface, JsonGenerator}
import spinal.lib.bus.regif.AccessType._

/*
 * the reg interface defines some operations to read/write the reg(maybe by bus interface)
 * not just like the bus slave factory , they can declare the reg file more easy
 * simulation : implement a reg bank which can be drive by the bus ( support bus : apb3,axilite4,ahblite3 )
*/

object RegInterface{
  class interruptControl() extends PrefixComponent{
    val io = new Bundle{
      val Interrupt = in Bool()
    }
  }

  class apbBank(bankNum:Int = 32) extends PrefixComponent{
    val io = new Bundle{
      val apb = slave (APBOperation.create(Apb3Config(addressWidth = 16,dataWidth = 16)))
    }

    val ctrl = new interruptControl()
    val Interrupt = ctrl.io.Interrupt.toIo()


    val busif = Apb3BusInterface(io.apb,(0x0000,1 MiB))  /* set mapping address */
    for(idx <- 0 until bankNum) yield new Area{
      val regfile = busif.newReg(doc = s"x${idx}")
      val field = regfile.field(Bits(16 bits),RW) /* create and define which field to read or write */
    }
    val rf = busif.newRegAt(address = 0x400,doc = "x_define")
    val field1 = rf.field(Bits(4 bits),RW,doc = "field1")
    val field2 = rf.field(Bits(4 bits),RO,doc = "field2")
    field2 := 3
    rf.reserved(8 bits)

    when(Interrupt) {
      io.apb.PREADY := False
    }

    /* generate the json report in the tmp job*/
    def genDoc() = {
      busif.accept(JsonGenerator("apbBank"))
      this
    }
    this.genDoc()
  }
}

object RegInterfaceSim extends App{
  import spinal.core.sim._
  SIMCFG(gtkFirst = true).compile{
    val dut = new RegInterface.apbBank(32)
    dut
  }.doSim{
    dut =>
      dut.clockDomain.forkStimulus(10)
      val driver = APBOperation.sim(dut.io.apb,dut.clockDomain)
      dut.Interrupt #= false
      /* drive the bus and see */
      driver.write(0x0004,100)
      val data = driver.read(0x0004)
      println("data is " + data)

      driver.write(0x400,1)
      val readonly = driver.read(0x400)
      println("readonly is " + readonly) /* 49 */

  }

}


