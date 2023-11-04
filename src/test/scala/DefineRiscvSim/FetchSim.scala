
package DefineRiscvSim

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
/* the Fetch unit simulation with get inst from TCM or DRAM */

class FetchSim extends AnyFunSuite {
  /* seems ready to fetch from the tcm*/
  test("fetch from the ITCM") {
    SIMCFG(compress = true).compile {
      val parameters = coreParameters(whiteBox = true) /* the pc starts from the 0x40000000 */
      val dut = new Fetch(parameters)
      /* one bank itcm with init code to be tested */
      BinTools.initRam(dut.Fetch.itcm.banks(0),"src/test/resources/TCM/dhrystoneIM/dhrystone.bin")
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
          dut.clockDomain.onSamplings {
            if (dut.io.fetchOut.valid.toBoolean) {
              val inst = HexStringWithWidth(dut.io.fetchOut.payload.instruction.toLong.toHexString,8)
              dut.io.fetchOut.payload.pc.toLong.toHexString match {
                case "40000b28" => assert(inst == "fd010113")
                case "40000c78" => assert(inst == "00560713")
                case "40000d14" => assert(inst == "0034c583")
                case "40001110" => assert(inst == "00c52603")
                case "40001210" => assert(inst == "00008067")
                case _ =>
              }
            }
        }
        def init() = {
          dut.io.halt #= false
          dut.io.flush #= false
          dut.io.pcLoad.valid #= false
          dut.io.pcLoad.payload #= 0
          dut.clockDomain.waitSampling()
        }
        init()

        def jump(pc:BigInt) = {
          dut.clockDomain.waitSampling(100)
          dut.io.pcLoad.valid #= true
          dut.io.pcLoad.payload #= pc
          dut.clockDomain.waitSampling()
          dut.io.pcLoad.valid #= false
          dut.clockDomain.waitSampling()
        }
        jump(0x40000d14)
        dut.clockDomain.waitSamplingWhere{
          dut.io.halt.randomize()
          dut.io.fetchOut.payload.pc.toLong.toHexString == "40001210"
        }
    }
  }

// Todo form the DRAM jump to the TCM (seems will ready)

  test("fetch from the Dram withOut Cache "){
    /* monitor it with the axi readOnly memory */
      SIMCFG(compress = true).compile {
        val parameters = coreParameters(whiteBox = true,resetValue = 0x80000000l)
        val dut = new FetchNoCache(parameters)
        dut
      }.doSim{
      dut =>
        dut.clockDomain.forkStimulus(10)
        BinTools.initRam(dut.fetch.Fetch.itcm.banks(0),"src/test/resources/TCM/dhrystoneIM/dhrystone.bin")
        dut.clockDomain.onSamplings {
          if (dut.fetch.io.fetchOut.valid.toBoolean) {
            val inst = HexStringWithWidth(dut.fetch.io.fetchOut.payload.instruction.toLong.toHexString, 8)
            dut.fetch.io.fetchOut.payload.pc.toLong.toHexString match {
              case "80000b28" => assert(inst == "fd010113")
              case "80000c78" => assert(inst == "00560713")
              case "80000d14" => assert(inst == "0034c583")
              case "80001110" => assert(inst == "00c52603")
              case "80001210" => assert(inst == "00008067")
              case "40000b28" => assert(inst == "fd010113")
              case "40000c78" => assert(inst == "00560713")
              case "40000d14" => assert(inst == "0034c583")
              case "40001110" => assert(inst == "00c52603")
              case "40001210" => assert(inst == "00008067")
              case _ =>
            }
          }
        }
        val sim = AxiReadonlyMemorysim(dut.io.bus,dut.clockDomain,AxiReadonlyMemorySimConfig(readResponseDelay = 30))
        AxiInit(dut.io.bus,readOnly = true)
        sim.memory.loadBinary(0x80000000l,"src/test/resources/DRAM/dhrystoneIM/dhrystone.bin")
        println("the file load the DRAM finish!")
        sim.start()

        def jump(pc: BigInt) = {
          dut.clockDomain.waitSampling(100)
          dut.fetch.io.pcLoad.valid #= true
          dut.fetch.io.pcLoad.payload #= pc
          dut.clockDomain.waitSampling()
          dut.fetch.io.pcLoad.valid #= false
          dut.clockDomain.waitSampling()
        }
        jump(0x40000d14)

        dut.clockDomain.waitSamplingWhere {
          dut.fetch.io.halt.randomize()
          dut.fetch.io.fetchOut.payload.pc.toLong.toHexString == "40001210"
        }
      }
    }

}
