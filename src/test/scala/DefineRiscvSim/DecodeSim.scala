package DefineRiscvSim

/* add the decode simulation here */
import DefineBus.Axi._
import DefineBus.SimpleBus.sim.SMBDriver
import DefineBus.SimpleBus.{SMBMemory, simpleBusConfig}
import DefineSim.SIMCFG
import DefineSim.SpinalSim.ScoreBoardSimulation
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._

import scala.util.Random
import DefineRiscv.Core.frontend._
import DefineRiscv.Core._
import spinal.lib.misc.{BinTools, HexTools}
import DefineSim.Logger.HexStringWithWidth
import DefineRiscv.Core._
import Untils.InstructionUntil._
/* for the decode purpose */

class DecodeSim extends AnyFunSuite {

  test("decode sim"){
    SIMCFG(compress = true).compile{
      val dut = new Decode(coreParameters())
      dut
    }.doSim{
      dut =>
        /* Test R type */
        dut.clockDomain.forkStimulus(10)
        val genR = RandomIR(iter = 200)

        /* add R type simulation here*/
    }





  }


}
