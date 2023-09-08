package DefineBus.Apb3
import spinal.lib.bus.amba3.apb._
import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import spinal.lib.fsm._

import scala.math._

 /*
 a simple memory as the apb drive it
 */

class Apb3Memory(config: Apb3Config,sync:Boolean = true) extends PrefixComponent{
  val io =new Bundle{
    val apb = slave(Apb3(config))
  }
  val depth = pow(2,config.addressWidth)
  val mem = Mem(Bits(config.dataWidth bits),depth.toInt)
  def operate(wr:Bool): Unit = {
    when(wr){
      mem.write(io.apb.PADDR,io.apb.PWDATA,enable = io.apb.PENABLE && io.apb.PREADY)
    }.otherwise{
      if (sync) {
        io.apb.PRDATA := mem.readSync(io.apb.PADDR, enable = io.apb.PENABLE && io.apb.PREADY)
      } else {
        io.apb.PRDATA := mem.readAsync(io.apb.PADDR)
      }
    }
  }
  //which cycle to get the data should be consider
  operate(io.apb.PWRITE)
}
