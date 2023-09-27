package DefineMemSim

import DefineMem._
import DefineSim._
import DefineSim.SpinalSim._
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import spinal.core._

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/*
 the custom cache should simulation very carefully using some random tests

 the logic is a little complex
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
          dut.clockDomain.waitSampling()
        }

        dut.clockDomain.forkStimulus(10)
        val initBoolean = ArrayBuffer[Bool]()
        initBoolean += dut.io.driver.cmd.valid
        initBoolean += dut.io.flush.cmd.valid
        SpinalSim.simInitValue(initBoolean, boolean = true, bits = false, clockDomain = dut.clockDomain)
        dut.clockDomain.waitSamplingWhere(!dut.haltCmd.toBoolean)
        println("flush cache finish")
        val addressList = SimUntils.GenRandomList(0,Int.MaxValue,10,true,prefix = "random address: ")
        val dataList = SimUntils.GenRandomList(0,Int.MaxValue,10,true,prefix = "random data: ")

        def hit(addressList:ArrayBuffer[BigInt],dataList:ArrayBuffer[BigInt]): Unit = {
          for ((address,data) <- (addressList,dataList).zipped) {
            dut.io.driver.cmd.valid #= false
            dut.io.driver.cmd.wr #= false
            val addressBinary =  Logger.bigintToBinaryStringWithWidth(address,32)
            val tagBinary = addressBinary.substring(10,32)
            val lineBinary = addressBinary.substring(5, 10)
            val wordBinary = addressBinary.substring(2, 10)
            val tag = Integer.parseInt(tagBinary, 2)
            val Taddress = Integer.parseInt(lineBinary, 2)
            val Daddress = Integer.parseInt(wordBinary, 2)
            println(s"tag: ${tag},Taddress:${Taddress},Daddress:${Daddress}")
            writeCache(Random.nextInt(4), Taddress, Daddress, tag, data, true)
            dut.io.driver.cmd.valid #= true
            dut.io.driver.cmd.address #= address
            dut.io.driver.cmd.wr #= false
            dut.io.driver.cmd.mask #= 0
            dut.io.driver.cmd.data #= 0

            dut.clockDomain.waitSamplingWhere(dut.io.driver.rsp.valid.toBoolean)
            println("hit data:" + dut.io.driver.rsp.data.toBigInt)
            println("hit address:" + dut.io.driver.rsp.address.toBigInt)
          }
        }

        // writeCache(0,0,0,0x102,0x10000000,true) /* this show a way to write something to the Cache file */

        /* test start flush */
        hit(addressList,dataList)
        MemoryContent(4,32,256) /* show one way value */
    }

  }
}
