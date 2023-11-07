package DefineMath.Divider

import spinal.core._
import spinal.lib._

object DivPlayerFixed{

  /* support the signed / unsigned and mixed div operation
  * but a little slow  the cycles is same as the width */

  def createMixed(nWidth : Int, dWidth : Int,storeDenominator : Boolean) = {
    val mixed = new MixedDivider(nWidth,dWidth,storeDenominator)
    mixed
  }

  def createSigned(nWidth : Int, dWidth : Int,storeDenominator : Boolean) = {
    val signed = new SignDivUnit(nWidth,dWidth,storeDenominator)
    signed
  }

  def createUnsigned(nWidth : Int, dWidth : Int,storeDenominator : Boolean) = {
    val unsigned = new UnsignDivUnit(nWidth,dWidth,storeDenominator)
    unsigned
  }

}
