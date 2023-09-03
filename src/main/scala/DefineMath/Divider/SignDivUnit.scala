package DefineMath.Divider

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
/*
this is the div unit from the lib.math at : https://github.com/SpinalHDL/SpinalHDL/blob/dev/lib/src/main/scala/spinal/lib/math/Divider.scala
read the code and test learn it
*/

case class SignDivCmd(nWidth:Int,dWidth:Int) extends Bundle{
  val numerator = SInt(nWidth bits)
  val denominator = SInt(dWidth bits)
}

case class SignDivRsp(nWidth:Int,dWidth:Int) extends Bundle{
  val quotient = SInt(nWidth bits)
  val remainder = SInt(dWidth bits)
  val error = Bool()
}

class SignDivUnit(nWidth:Int,dWidth:Int,storeDenominator:Boolean) extends PrefixComponent {
  val io = new Bundle{
    val flush = in Bool()
    val cmd = slave Stream(SignDivCmd(nWidth, dWidth))
    val rsp = master Stream(SignDivRsp(nWidth, dWidth))
  }
  val divider = new UnsignDivUnit(nWidth,dWidth,storeDenominator,Bits(2 bit))
  divider.io.flush := io.flush
  divider.io.cmd.arbitrationFrom(io.cmd)
  divider.io.cmd.numerator := io.cmd.numerator.abs
  divider.io.cmd.denominator := io.cmd.denominator.abs
  divider.io.cmd.context(0) := (io.cmd.numerator.msb ^ io.cmd.denominator.msb)
  divider.io.cmd.context(1) := io.cmd.numerator.msb

  io.rsp.arbitrationFrom(divider.io.rsp)
  io.rsp.quotient := divider.io.rsp.quotient.twoComplement(divider.io.rsp.context(0)).resized
  io.rsp.remainder := divider.io.rsp.remainder.twoComplement(divider.io.rsp.context(1)).resized
  io.rsp.error := divider.io.rsp.error
}

/*
Mixed the sign with the cmd signed signal
*/

case class MixedDivCmd(nWidth : Int, dWidth : Int) extends Bundle{
  val numerator = Bits(nWidth bit)
  val denominator = Bits(dWidth bit)
  val signed = Bool()
}
case class MixedDivRsp(nWidth : Int, dWidth : Int) extends Bundle{
  val quotient = Bits(nWidth bit)
  val remainder = Bits(dWidth bit)
  val error = Bool()
}
class MixedDivider(nWidth : Int, dWidth : Int,storeDenominator : Boolean) extends PrefixComponent{
  val io = new Bundle{
    val flush = in Bool()
    val cmd = slave Stream(MixedDivCmd(nWidth,dWidth))
    val rsp = master Stream(MixedDivRsp(nWidth,dWidth))
  }
  val divider = new UnsignDivUnit(nWidth,dWidth,storeDenominator,Bits(2 bit))

  divider.io.flush := io.flush
  divider.io.cmd.arbitrationFrom(io.cmd)
  divider.io.cmd.numerator := io.cmd.numerator.asSInt.abs(io.cmd.signed)
  divider.io.cmd.denominator := io.cmd.denominator.asSInt.abs(io.cmd.signed)
  divider.io.cmd.context(0) := io.cmd.signed && (io.cmd.numerator.msb ^ io.cmd.denominator.msb)
  divider.io.cmd.context(1) := io.cmd.signed && io.cmd.numerator.msb

  io.rsp.arbitrationFrom(divider.io.rsp)
  io.rsp.quotient := divider.io.rsp.quotient.twoComplement(divider.io.rsp.context(0)).asBits.resized
  io.rsp.remainder := divider.io.rsp.remainder.twoComplement(divider.io.rsp.context(1)).asBits.resized
  io.rsp.error := divider.io.rsp.error
}