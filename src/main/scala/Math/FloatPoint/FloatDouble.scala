package lib.Math.FloatPoint

import java.lang.Float._

object Fp32 {
  def expWidth = 8

  def expOffset = (1l << expWidth) - 1

  def mantWidth = 23

  def mantOffset = (1l << expWidth) - 1

  def bias = 127

  def asBits(f: Float) = Misc.asBits[Float](f)

  def asFloat(i: Int) = {
    intBitsToFloat(i)
  }

  def signal(f: Float) = Misc.signal[Float](f, expWidth, mantWidth)

  def exp(f: Float) = Misc.exp[Float](f, mantWidth, expOffset)

  def mant(f: Float) = Misc.mant[Float](f, mantOffset)

  //some value function to judge
  def isDenormal(f: Float) = Misc.isDenormal[Float](f, mantWidth, expOffset, mantOffset)

  def isZero(f: Float) = Misc.isZero[Float](f, mantWidth, expOffset, mantOffset)

  def isNaN(f: Float) = Misc.isNan[Float](f)

  def isInfinite(f: Float) = Misc.isInfinite[Float](f)

  def isRegular(f: Float) = Misc.isRegular[Float](f, mantWidth, expOffset, mantOffset)

  def show_bits(f: Float): Unit = {
    printf("    %15e  %08x", f, Fp32.asBits(f))
  }
  //show the formal
  def print(f: Float) = Misc.print[Float](f,expWidth,mantWidth,exp(f),mant(f),signal(f))
}

object Fp64 {
  def expWidth = 11

  def expOffset = (1L << expWidth) - 1

  def mantWidth = 52

  def mantOffset = (1L << mantWidth) - 1

  def bias = 1023

  //can add more function with double
  def asBits(d: Double) = Misc.asBits[Double](d)

  def signal(d: Double) = Misc.signal[Double](d, expWidth, mantWidth)

  def exp(d: Double) = Misc.exp[Double](d, mantWidth, expOffset)

  def mant(d: Double) = Misc.mant[Double](d, mantOffset)

  def isRegular(d: Double) = Misc.isRegular[Double](d, mantWidth, expOffset, mantOffset)

  def isDenormal(d: Double) = Misc.isDenormal[Double](d, mantWidth, expOffset, mantOffset)

  def print(d: Double) = Misc.print[Double](d,expWidth,mantWidth,exp(d),mant(d),signal(d))
}

//test with print of float or double case
object show extends App{
  Fp64.print(1.78)
  println()
  Fp32.print(1.78f)
}