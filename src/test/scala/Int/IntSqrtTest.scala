package lib.Int

import lib.Sim.SpinalSim
import lib.Math.Int.IntSqrt
import spinal.core.sim._
import org.scalatest.funsuite.AnyFunSuite

class IntSqrtTest extends AnyFunSuite{
  var compiled:SimCompiled[IntSqrt] = null
  test("compile"){
    compiled = SpinalSim().compile(new IntSqrt(5,5))
  }

  test("testbench"){
    compiled.doSim(seed = 42){
      dut =>
        dut.clockDomain.forkStimulus(10)
    }
  }
}
