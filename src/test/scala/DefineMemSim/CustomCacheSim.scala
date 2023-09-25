package DefineMemSim

import DefineMem._
import DefineSim.{Logger, SIMCFG, SpinalSim}
import DefineSim.SpinalSim._
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import spinal.core._

import scala.collection.mutable.ArrayBuffer

/*
 the custom cache should simulation very carefully using some random tests
*/

class CustomCacheSim extends AnyFunSuite {
  test("Readonly InstructionCache") {
    SIMCFG(gtkFirst = true).compile {
      val config = cacheConfig(cacheSize = 4096, bytePerLine = 32, wayCount = 4, addressWidth = 32,
        cmdDataWidth = 32, memDataWidth = 32, readOnly = true)
      val dut = new CustomCache(config, "InstructionCache")
      addSimPublic(List(dut.haltCmd))
      dut
    }.doSim {
      dut =>
        def MemoryContent(ways: Int, linecounter: Int, waywordcount: Int): Unit = {
          for (way <- 0 until ways) {
            println(s"==================== way : ${way}=====================")
            println("---------------------tags-----------------------")
            for (idx <- 0 until linecounter) {
              val value = Logger.bigintToBinaryStringWithWidth(dut.ways(way).tags.getBigInt(idx),23)
              print(s"$value" + "\t")
              if ((idx + 1) % 4 == 0) {
                print('\n')
              }
            }
            println("---------------------data-----------------------")
            for (idx <- 0 until waywordcount) {
              print(dut.ways(way).datas.getBigInt(idx) + "\t")
              if ((idx + 1) % 8 == 0) {
                print('\n')
              }
            }
          }
        }

        def writeCache(way:Int,Taddress:Long,Daddress:Long,tags:BigInt,data:BigInt,valid:Boolean) = {
          if(valid){
            dut.ways(way).tags.setBigInt(Taddress,tags)
            dut.ways(way).datas.setBigInt(Daddress,data)
          }
          else {
            dut.ways(way).datas.setBigInt(Daddress,data)
          }
        }

        dut.clockDomain.forkStimulus(10)
        val initBoolean = ArrayBuffer[Bool]()
        initBoolean += dut.io.driver.cmd.valid
        initBoolean += dut.io.flush.cmd.valid
        SpinalSim.simInitValue(initBoolean, boolean = true, bits = false, clockDomain = dut.clockDomain)
        dut.clockDomain.waitSamplingWhere(!dut.haltCmd.toBoolean)

        writeCache(0,0,0,0x102,0x10000000,true) /* this show a way to write something to the Cache file */

        /* test start flush */
        MemoryContent(4,32,256) /* show one way value */
    }

  }
}
