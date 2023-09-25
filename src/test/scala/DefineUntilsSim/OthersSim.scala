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

class OthersSim extends PrefixComponent{
  val io = new Bundle{
    val value = in UInt(4 bits)
    val boolean = in Bool()
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