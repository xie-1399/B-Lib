package DefineUntilsSim

import DefineSim._
import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import spinal.sim._
import DefineUntils.Others
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class OthersSim extends PrefixComponent{
  val io = new Bundle{
    val value = in UInt(4 bits)
  }
  val delay = Others.delay(io.value,5)
  val history = Others.history(io.value,10)
}

class OtherTest extends AnyFunSuite{
  test("others until component"){
    SIMCFG(gtkFirst = true).compile{
     val dut = new OthersSim()
     dut
    }.doSim{
      dut =>
        dut.clockDomain.forkStimulus(10)
        def operation() = {
          dut.io.value #= Random.nextInt(15)
        }

        SpinalSim.onlySample(dut.clockDomain,operation = operation)
    }
  }

}