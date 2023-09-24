package DefineBusSim

import DefineBus.SimpleBus._
import DefineBus.SimpleBus.sim.SMBDriver
import DefineSim._
import org.scalatest.funsuite.AnyFunSuite
import spinal.sim._
import spinal.core.sim._
import scala.util.Random
import scala.collection.mutable.ArrayBuffer
import DefineSim.SpinalSim._

/*
 test about the define simple bus
*/

class SMBTest extends AnyFunSuite {

  test("SMB async/sync memory pass all") {
    SIMCFG(gtkFirst = true).compile {
      val config = simpleBusConfig(16,16,2) // 8 bits mask
      val dut = new SMBMemory(config,sync = false)
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        val driver = new SMBDriver(dut.io.smb,dut.clockDomain)
        val scoreboardInOrder = ScoreBoardSimulation[IndexedSeq[BigInt]]()
        val iteration  = 1000
        val DataList = for(idx <- 0 until iteration) yield BigInt(Random.nextInt(100) * 5 + idx)
        scoreboardInOrder.pushRef(DataList)
        for(idx <- 0 until iteration){
          driver.write(idx,DataList(idx),mask = 3)
        }
        val readArray = for(idx <- 0 until iteration) yield driver.read(idx,sync = false)
        scoreboardInOrder.pushDut(readArray)
    }
  }
}
