package DefineMath.HardFloat

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._
import spinal.lib.experimental.math.{Floating, Floating16, Floating32}

/* the spinal libs has achieve the float which support
*  (1) IEEE-754 floating format
*  (2) Recoded floating format */

object Play extends App {

  /* in fact lots of functions are missing */

  class floatDemo() extends PrefixComponent{
    val fp = Floating16()
    fp := 10.2

    val fp2 = Floating32()
    fp2 := -19.2

    val bool = fp.toRecFloating > fp2.toRecFloating
  }

  val rtl = new RtlConfig().GenRTL(top = new floatDemo())

}
