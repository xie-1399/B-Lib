package DefineUntilsSim

import DefineSim._
import DefineSim.SpinalSim.{PrefixComponent, addSimPublic}
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import spinal.sim._
import DefineUntils.Others
import DefineUntils.CounterUntil
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

case class NewBundle() extends Bundle{
  val useBool = Bool()
  val useInt = UInt(10 bits)
  val useBits = Bits(10 bits)
}

class OthersSim extends PrefixComponent{
  val io = new Bundle{
    val value = in UInt(4 bits)
    val boolean = in Bool()
    val newBundle = out (NewBundle())
  }

  val useboolean = False
  when(io.boolean){useboolean := True} /* will convert it to reg */
  val majorityVote = MajorityVote(io.value)
  val onenumber = Others.getOneNumber(io.value.asBools) > io.value.asBools.size / 2
  assert(majorityVote === onenumber)

  val keepcounter = CounterUntil.keepCounter(0,10)
  keepcounter.increment()

  val delay = Others.delay(io.value,5)
  val history = Others.history(io.value,10)

  Others.assignBundleWithList(io.newBundle,Seq(True,U(10,10 bits),B(0,10 bits)))

  val e1 = B"1111"
  val e2 = B"1011"
  val e3 = B"0011"
  val data1 = B"0101"
  val data2 = B"1011"
  val test1 = Others.equalWithList(data1,Seq(e1,e2,e3))
  val test2 = Others.equalWithList(data2,Seq(e1,e2,e3))


  val wire_reg = RegInit(False) /* not like False */
  when(io.boolean){wire_reg := True}
}


class OtherTest extends AnyFunSuite{
  test("others until component"){
    SIMCFG(gtkFirst = true).compile{
     val dut = new OthersSim()
     addSimPublic(List(dut.keepcounter))
     dut
    }.doSim{
      dut =>
        dut.clockDomain.forkStimulus(10)
        def operation() = {
          dut.io.value #= Random.nextInt(15)
        }
        SpinalSim.onlySample(dut.clockDomain,operation = operation)
        for(idx <- 0 until 100){
          assert(dut.keepcounter.value.toBigInt == 10)
          dut.clockDomain.waitSampling()
        }

    }
  }

}