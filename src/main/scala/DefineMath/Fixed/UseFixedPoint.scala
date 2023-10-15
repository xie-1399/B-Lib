package DefineMath.Fixed

import DefineSim.SpinalSim._
import DefineSim._
import spinal.core._
import spinal.lib._

/*
  * the spinal hdl has UFix and SFix type value
  * this seems can reuse it as the data type
  * how to represent fixed point at: https://blog.csdn.net/weixin_42454243/article/details/125103803
  * the material : https://spinalhdl.github.io/SpinalDoc-RTD/dev/SpinalHDL/Data%20types/Fix.html */

object UseFixedPoint{

  class FixedPointDemo() extends PrefixComponent{
    val io = new Bundle{
      val adder = in Bool()
    }
    val reg = RegInit(False)
    val UFixed = UFix(peak = 8 exp,width = 10 bits)
    /* or declare like: val UFixed = UFix(peak = 8 exp,resolution = -2 exp) */
    val UFixedop = UFix(peak = 8 exp,width = 10 bits)
    val SFixed = SFix(peak = 8 exp,width = 10 bits)

    /* assign with fixed point like this */
    UFixed := 1.25
    SFixed.raw := S(17)
    UFixedop.raw := U(17)
    println("maxValue: " + UFixed.maxValue)
    val addFixed = UFixedop + UFixed
  }
}

object FixedPointDemo extends App{
  /* the wave sim should be represent as fixed also */
  import spinal.core.sim._
  import UseFixedPoint._
  SIMCFG(gtkFirst = true).compile{
    val dut = SpinalVerilog(new FixedPointDemo())
    dut
  }.doSim{
    dut =>
      dut.clockDomain.forkStimulus(10)
      dut.clockDomain.waitSampling(5)
  }




}

