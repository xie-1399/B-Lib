package DefineDevice.Interrupt

import spinal.core._
import spinal.lib.bus.misc.BusSlaveFactory

/* the platform-level interrupt controller -> control all external interrupt to the target
  * the material : https://github.com/riscv/riscv-plic-spec/blob/master/riscv-plic.adoc
  * should know the material first*/
object PLicSource {

  abstract class PlicGateWay(id:Int,priorityWidth:Int) extends Area{
    val ip:Bool
    val priority = UInt(priorityWidth bits)
    def doClaim():Unit
    def doCompletion():Unit
    def driveFrom(bus:BusSlaveFactory,offset:Int):Unit
  }



}


object PLic{



}