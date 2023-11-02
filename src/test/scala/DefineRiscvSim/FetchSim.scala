
package DefineRiscvSim

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
/* the Fetch unit simulation with get inst from TCM or DRAM */

class FetchSim extends AnyFunSuite {

  test("fetch from the ITCM") {
    SIMCFG(gtkFirst = true).compile {
      val parameters = coreParameters(whiteBox = true) /* the pc starts from the 0x10000000 */
      val dut = new Fetch(parameters)
      /* one bank itcm with init code to be tested */
      BinTools.initRam(dut.Fetch.itcm.banks(0),"src/test/resources/TCM/dhrystoneIM/dhrystone.bin")
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)

        def init() = {
          dut.io.halt #= false
          dut.io.flush #= false
          dut.io.pcLoad.valid #= false
          dut.io.pcLoad.payload #= 0
          dut.clockDomain.waitSampling()
        }
        init()



    }
  }


  test("fetch from the Dram withOut Cache "){
    /* monitor it with the axi readOnly memory */
  }

  test("with pcLoad jump and halt the cpu test "){

  }

}
