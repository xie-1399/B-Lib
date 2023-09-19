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
        //val data = input.toList
        //val id = magic.toList
        val data = List(2,2,3,4,6,5,6,5,4,8)
        val id = List(4,3,2,1,0,9,8,7,6,5)
        dut.io.write #= false
        dut.clockDomain.waitSampling(5)

        def write(data:List[Int],id:List[Int]) = {
          dut.io.write #= true
          for(idx <- 0 until 10){
            dut.io.data(idx) #= data(idx)
            dut.io.id(idx) #= id(idx)
          }
          dut.clockDomain.waitSampling()
          dut.io.write #= false
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
