package DefineUntils

import DefineSim.SIMCFG
import DefineSim.SpinalSim.PrefixComponent
import DefineUntils.Defineencoding.{four, newElement, one, three, two}
import spinal.core._
import spinal.lib._
import DefineSim.SimUntils._
import spinal.core.sim._
/*
 * here is an example for using the SpinalEnum writing player
 * show about the simulation about the encoding
 * and think about the data type change when happens */

object Defineencoding extends SpinalEnum{
  val one,two,three,four = newElement()
  /* here shows about the a define encoding format */
  defaultEncoding = SpinalEnumEncoding("define")(
    one -> 3,
    two -> 4,
    three -> 5,
    four -> 7
  )
}

object sequenceEncoding extends SpinalEnum(binarySequential){
  /* the usage encode format like this : binarySequential binaryOneHot graySequential */
  val one,two,three,four = newElement() /* in that case will represent with sequence bits*/
}

class SpinalEnumPlayer extends PrefixComponent{

  val io = new Bundle{
    val driver = in Bits(3 bits)
    val simIn = in (Defineencoding())
    val raw = in Bits(4 bits)
  }
  /* so as bits is not signal like */
  val get_one = io.driver === Defineencoding.one.asBits
  val get_two = io.driver === Defineencoding.two.asBits
  val get_three = io.driver === Defineencoding.three.asBits
  val get_four = io.driver === Defineencoding.four.asBits
  val toSint = io.raw.asSInt
  val toUint = io.raw.asUInt

  val whiteBox = new Area{
    toSint.simPublic()
    toUint.simPublic()
  }
}

/* using the spinal Enum sim tools to get the real encoding value */
object SpinalEnumPlayer extends App{
  import spinal.core.sim._
  import DefineSim.SpinalSim
  SIMCFG(compress = true).compile{
    val dut = new SpinalEnumPlayer()
    dut
  }.doSim{
    dut =>
      dut.clockDomain.forkStimulus(10)

      dut.io.raw.randomize()
      dut.clockDomain.onSamplings{
        /* so if you want to get the value should use like this */
        println(dut.toSint.toBigInt)
        println(dut.toUint.toLong)
        //println(getEnumEncodingValue(dut.io.simIn))  /* the to bigInt will show the number in order */
      }

      def operation() = {
        dut.io.driver.randomize()
        dut.io.simIn.randomize() /* one in the show */
      }
      SpinalSim.onlySample(dut.clockDomain,operation = operation)
  }

}