package DefineUntilsSim

import DefineSim.{SIMCFG, SpinalSim}
import DefineSim.SpinalSim.addSimPublic
import DefineUntils.Switch.CrossbarConfig
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import scala.util.Random

class CrossbarSim extends AnyFunSuite{
  test("crossbar simulation"){
    SIMCFG(gtkFirst = true).compile{
      val dut = new CrossbarConfig(size = 10,dataWidth = 16,idWidth = 4,withFifo = true)
      dut
    }.doSim{
      dut =>
        dut.clockDomain.forkStimulus(10)
        val input = for(idx <- 0 until 10) yield idx
        val magic = for(idx <- 0 until 10) yield 9 - idx
        val data = input.toList
        val id = magic.toList

        def write(data:List[Int],id:List[Int]) = {
          for(idx <- 0 until 10){
            dut.io.write #= true
            dut.io.data(idx) #= data(idx)
            dut.io.id(idx) #= id(idx)
            dut.clockDomain.waitSampling()
          }
          dut.clockDomain.waitSampling(10)
        }

        def show() = {
          for(idx <- 0 until 10) {println(dut.io.cmd(idx).toBigInt)}
        }

        write(data,id)
        show()

    }
  }

}