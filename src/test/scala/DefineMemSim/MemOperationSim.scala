package DefineMemSim
import DefineSim.SpinalSim._
import spinal.core._
import DefineSim._
import spinal.lib._
import spinal.sim._
import spinal.core.sim._
import scala.collection.mutable.ArrayBuffer
import DefineMem._
import org.scalatest.funsuite.AnyFunSuite

  /*
  the simulation is about the ram
  the ram has so many untils and some operations about it
  */

class MemOperationSim extends AnyFunSuite {
  test("Mem operation component") {
    SIMCFG(gtkFirst = true).compile {
      val dut = new Component {
        val ram = MemOperation.apply(UInt(3 bits),8,2)
      }
      addSimPublic(mems = List(dut.ram))
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        dut.clockDomain.waitSampling()
        for (idx <- 0 until 7){
            println(MemOperation.getSim(dut.ram,idx))}  //assert it === 2
    }
  }

}


