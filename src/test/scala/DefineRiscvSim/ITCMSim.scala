package DefineRiscvSim

import DefineBus.SimpleBus.sim.SMBDriver
import DefineBus.SimpleBus.{SMBMemory, simpleBusConfig}
import DefineRiscv.Core.{ITCM, ITCMParameters, coreParameters}
import DefineSim.SIMCFG
import DefineSim.SpinalSim.{ScoreBoardSimulation, addSimPublic}
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import DefineSim.SimUntils.GenRandomList
import spinal.lib.sim.{FlowMonitor, StreamDriver}
import DefineSim.Logger._
import spinal.lib.misc.HexTools

import scala.collection.mutable
import scala.util.Random
import scala.collection.mutable.ArrayBuffer

class ITCMSim extends AnyFunSuite {

  test("itcm read test ") {
    SIMCFG(compress = true).compile {
      val dut = new ITCM(coreParameters(), ITCMParameters())
      addSimPublic(mems = List(dut.banks(0), dut.banks(1), dut.banks(2), dut.banks(3)))
      dut
    }.doSimUntilVoid {
      dut =>
        dut.clockDomain.forkStimulus(10)
        val banks = dut.banks

        /* load some values to the memory */
        def load(bankId: Int) = {
          val randomList = GenRandomList(1, 100, 1024)
          for (idx <- 0 until 1024) {
            banks(bankId).setBigInt(idx, randomList(idx))
          }
        }

        def get(bankId: Int) = {
          val array = ArrayBuffer[BigInt]()
          for (idx <- 0 until 1024) {
            array += banks(bankId).getBigInt(idx)
          }
          array
        }

        def signleBank(bankId:Int) = {
          load(bankId)
          // dut.io.flush #= false
          dut.clockDomain.waitSampling()
          /* wait sampling to the memory */
          val ref = get(bankId)
          val queue = mutable.Queue[BigInt]()
          val Pcqueue = mutable.Queue[BigInt]()
          val scoreboardInOrder = ScoreBoardSimulation[String]()

          StreamDriver(dut.io.request.fetchCmd, dut.clockDomain) {
            payload =>
              dut.io.request.fetchCmd.payload.io #= true
              val pc = 0x10000000 + 1024 * bankId + 4 * Random.nextInt(256)
              dut.io.request.fetchCmd.payload.pc #= pc
              Pcqueue.enqueue(pc)
              queue.enqueue(pc)
              true
          }

          FlowMonitor(dut.io.request.fetchRsp, dut.clockDomain) {
            payload =>
              val address = (queue.dequeue() - 0x10000000 - (1024 * bankId)).toInt
              val refString = ref(address + 3).toLong.toHexString + HexStringWithWidth(ref(address + 2).toLong.toHexString, 2) +
                HexStringWithWidth(ref(address + 1).toLong.toHexString, 2) + HexStringWithWidth(ref(address + 0).toLong.toHexString, 2)
              println("dut:" + payload.instruction.toLong.toHexString)
              println("ref:" + refString)
              scoreboardInOrder.pushDut(payload.instruction.toLong.toHexString)
              scoreboardInOrder.pushRef(refString)
              assert(payload.pc.toLong.toHexString == Pcqueue.dequeue().toLong.toHexString)
          }
          dut.clockDomain.waitActiveEdgeWhere(scoreboardInOrder.matches == 10000)
        }

        def MulBank() = {
          for(idx <- 0 until 4){
            load(idx)
          }
          // dut.io.flush #= false
          dut.clockDomain.waitSampling()
          /* wait sampling to the memory */
          val queue = mutable.Queue[BigInt]()
          val Pcqueue = mutable.Queue[BigInt]()
          val scoreboardInOrder = ScoreBoardSimulation[String]()

          StreamDriver(dut.io.request.fetchCmd, dut.clockDomain) {
            payload =>
              dut.io.request.fetchCmd.payload.io #= true
              val pc = 0x10000000 + 4 * Random.nextInt(1024)
              dut.io.request.fetchCmd.payload.pc #= pc
              Pcqueue.enqueue(pc)
              queue.enqueue(pc)
              true
          }

          FlowMonitor(dut.io.request.fetchRsp, dut.clockDomain) {
            payload =>
              val real = queue.dequeue() - 0x10000000
              val address = (real.toInt) % 1024
              val idx = (real.toInt) / 1024
              val ref = get(idx)
              val refString = ref(address + 3).toLong.toHexString + HexStringWithWidth(ref(address + 2).toLong.toHexString, 2) +
                HexStringWithWidth(ref(address + 1).toLong.toHexString, 2) + HexStringWithWidth(ref(address + 0).toLong.toHexString, 2)

              println("dut:" + payload.instruction.toLong.toHexString)
              println("ref:" + refString)
              scoreboardInOrder.pushDut(payload.instruction.toLong.toHexString)
              scoreboardInOrder.pushRef(refString)
              assert(payload.pc.toLong.toHexString == Pcqueue.dequeue().toLong.toHexString)
          }
          dut.clockDomain.waitActiveEdgeWhere(scoreboardInOrder.matches == 100000)
        }
        MulBank()
        simSuccess()
    }
  }

  test("flush it"){
    SIMCFG(compress = true).compile {
      val dut = new ITCM(coreParameters(), ITCMParameters(withFlush = true))
      addSimPublic(mems = List(dut.banks(0), dut.banks(1), dut.banks(2), dut.banks(3)))
      dut
    }.doSimUntilVoid {
      dut =>
        dut.clockDomain.forkStimulus(10)
        dut.io.flush #= true
        dut.io.request.fetchCmd.valid #= false
        dut.clockDomain.waitSampling(1024)
        for(idx <- 0 until 100000){
          val addr = 4 * Random.nextInt(256)
          assert(dut.banks(Random.nextInt(4)).getBigInt(addr) == 0)
        }
        simSuccess()
    }
  }


  test("init the itcm code"){
    SIMCFG(compress = true).compile {
      val dut = new ITCM(coreParameters(), ITCMParameters(withFlush = true,TCMBlock = 1,TCMDepth = 16384))
      addSimPublic(mems = List(dut.banks(0)))
      HexTools.initRam(dut.banks(0),"src/test/resources/add.hex",hexOffset = 0x80000000l)
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        dut.io.flush #= false
        dut.io.request.fetchCmd.valid #= false
        dut.clockDomain.waitSampling()

        /* read some instructions see */
        def read(address:BigInt) = {
          dut.io.request.fetchCmd.valid #= true
          dut.io.request.fetchCmd.pc #= address
          dut.io.request.fetchCmd.io #= true
          dut.clockDomain.waitSamplingWhere(dut.io.request.fetchRsp.valid.toBoolean)
          println("dut : " + HexStringWithWidth(dut.io.request.fetchRsp.payload.instruction.toLong.toHexString,8))
          dut.io.request.fetchCmd.valid #= false
          dut.clockDomain.waitSampling(10)
        }
        /* seems ready for it the add.hex ref: 04c0006f 34202f73 00800f93 03ff0a63 */
        read(0x80000000l)
        read(0x80000004l)
        read(0x80000008l)
        read(0x8000000Cl)
    }
  }
}
