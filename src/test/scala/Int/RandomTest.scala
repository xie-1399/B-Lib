package lib.Int

import spinal.core.sim._
import org.scalatest.funsuite.AnyFunSuite
import lib.Math.Int.RandomGenerator
import lib.Sim.SpinalSim

import scala.util.Random

class RandomTest extends AnyFunSuite{
  var compiled:SimCompiled[RandomGenerator] = null
  test("compile"){
    compiled = SpinalSim().compile(new RandomGenerator(32))
  }

  test("testbench"){
    compiled.doSim(seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)
        def operation() = {
          dut.io.valid #= Random.nextInt(5) > 2
        }
        SpinalSim.onlySample(dut.clockDomain,operation = operation, iter = 50)
    }
  }
}
