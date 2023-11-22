package DefineRiscvSim.Untils

import scala.util.Random
import DefineSim.Logger._

object SimUntils {

  def generateOP(dataWidth: Int, bias: Boolean = false, over: Boolean = false) = {
    if (bias && over) {
      throw new Exception("gen op error")
    }
    var op = ""
    for (idx <- 0 until dataWidth) {
      if (idx == 0 && (bias || over)) {
        if (bias) op += "1"
        if (over) op += "0"
      } else {
        val seed = Random.nextInt(10)
        val ch = if (seed > 5) "1" else "0"
        op += ch
      }
    }
    op
  }

  def shift(bin: String, dataWidth: Int, shiftValue: Int, left: Boolean = true, logic: Boolean = true) = {
    require(bin.length <= dataWidth)
    /* the left will fill with 0 and right may fill signal */
    val value = BigInt(bin, 2)
    if (left) {
      val temp = (value << shiftValue).toLong.toBinaryString
      val leftBin = if (temp.length < dataWidth) HexStringWithWidth(temp, dataWidth) else temp.substring(temp.length - dataWidth, temp.length)
      leftBin
    } else {
      val temp = HexStringWithWidth(bin, dataWidth)
      val sign = temp(0)
      if (logic) {
        ("0" * shiftValue) + temp.substring(0, dataWidth - shiftValue)
      } else {
        (sign.toString * shiftValue) + temp.substring(0, dataWidth - shiftValue)
      }
    }
  }

}