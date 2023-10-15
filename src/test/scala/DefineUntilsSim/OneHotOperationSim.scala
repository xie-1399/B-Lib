package DefineUntilsSim
import DefineUntils._
import DefineSim.SpinalSim._
import DefineSim._
import org.scalatest.funsuite.AnyFunSuite
import spinal.core._
import spinal.sim._
import spinal.core.sim._
import scala.util.Random
import scala.math._
import ProMux.OneHotOperation
/*
 the Component is used to test about the one hot operation
 the asBools is the same bit of Bool presentation
 also add the logger sim in it
*/

class OneHotOperationSim extends PrefixComponent{
  val io = new Bundle{
    val Invalue = in UInt(3 bits)
    val Inbools = in Vec(Bool(),4)
    val InUInts = in Vec(UInt(3 bits),4)
    val selMapping = in UInt(3 bits)
    val InSInt = in SInt(3 bits)
    val InBits = in Bits(3 bits)

  }
  val InvalueBools = io.Invalue.asBools
  val UIntOh = OneHotOperation.UInt2Oh(io.Invalue)  // 3 -> (....1000)
  val UIntOhBools = OneHotOperation.UInt2OhBools(io.Invalue)
  val Mapping = List[Int](1,2,3,4)
  val UIntmapping = OneHotOperation.UIntMapping(io.selMapping,Mapping) // 3 -> 0100

  val OhUInt = OneHotOperation.BoolsOh2UInt(io.Inbools)  //(false,true,true,true) -> 3 , the list last value
  val OhBitvectorUInt = OneHotOperation.BitVectorOh2UInt(io.Invalue)  // 100 -> 2,should care of the log2(up) number

  /*
  true,true,true,false -> last (0010) first(1000)
  */
  val firstLSB = OneHotOperation.BoolswordLSBOh(io.Inbools) //use v2
  val last = OneHotOperation.BoolswordMSBOh(io.Inbools) //use v2
}


class OneHotOperationTest extends AnyFunSuite{
  test("one hot sim"){
    SIMCFG(gtkFirst = true).compile{
     val dut = new OneHotOperationSim
     addSimPublic(List(dut.InvalueBools,dut.UIntOh,dut.UIntmapping,dut.OhUInt,dut.OhBitvectorUInt,dut.firstLSB(0),dut.firstLSB(1),dut.firstLSB(2),dut.firstLSB(3)))
     dut
    }.doSim{
    dut =>
        dut.clockDomain.forkStimulus(10)

        def setSInt() = {
          dut.io.InSInt #= -1
          dut.clockDomain.waitSampling()
          Logger(dut.io.InSInt,binary = false)

          dut.io.InSInt #= -2
          dut.clockDomain.waitSampling()
          Logger(dut.io.InSInt, binary = true)
        }

        def setBits() = {
          dut.io.InBits #= 4
          dut.clockDomain.waitSampling()
          Logger(dut.io.InBits,signal = true,bitWidth = 3)

          dut.io.InBits #= 4
          dut.clockDomain.waitSampling()
          Logger(dut.io.InBits, signal = false,bitWidth = 3)
        }

        def setUInt() = {
          val value = Random.nextInt(8)
          dut.io.Invalue #= value
          dut.clockDomain.waitSampling()
          Logger(dut.io.Invalue)
          assert(dut.UIntOh.toInt == pow(2,value))
          dut.io.Invalue #= 4   //100
          dut.clockDomain.waitSampling()
        }

        def setSelMapping() = {
          val value = Random.nextInt(5)
          dut.io.selMapping #= value
          dut.clockDomain.waitSampling()
          assert(dut.UIntmapping.getWidth == 4)
          if(value == 4){
            assert(dut.UIntmapping.toInt == 8)
          }
        }

        def setBools() = {
          val array = List(false,true,true,false)
          for(idx <- 0 until 4){
            dut.io.Inbools(idx) #= array(idx)
          }
          Logger(dut.io.Inbools(0),binary = true)
          dut.clockDomain.waitSampling()
          for(idx <- 0 until 4){
            println(dut.firstLSB(idx).toBigInt)
          }
        }

        onlySample(dut.clockDomain,operation = setBits)
    }
  }

}