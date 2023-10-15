package DefineUntils.BlackBox

import DefineSim.SpinalSim._
import spinal.core._
import spinal.lib._

  /*
 just see a stand black use of the inline verilog
 in fact you should notice the module name
  */

class InlineBlackBoxAdder(RTLPath: String) extends BlackBox {
  //set the clock
  val io = new Bundle {
    val clk = in Bool()
    val a = in UInt (32 bits)
    val b = in UInt (32 bits)
    val cin = in Bool()
    val c = out UInt (32 bits)
    val cout = out Bool()
  }
  noIoPrefix()
  //map the clock
  mapCurrentClockDomain(io.clk)
  addRTLPath(RTLPath)
}

class boxtest extends PrefixComponent {
  val io = new Bundle{
    val a = in UInt (32 bits)
    val b = in UInt (32 bits)
    val cin = in Bool()
    val c = out UInt (32 bits)
    val cout = out Bool()
  }

  val blackBox = new InlineBlackBoxAdder("rtl/inlineblackbox.v")
  blackBox.io.a := io.a
  blackBox.io.b := io.b
  blackBox.io.cin := io.cin
  io.c := blackBox.io.c
  io.cout := blackBox.io.cout
}

object BlackBoxOperation extends App {
  val rtl = new RtlConfig().GenRTL(top = new boxtest())
}
