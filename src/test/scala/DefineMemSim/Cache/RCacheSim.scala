package DefineMemSim.Cache

import DefineMem.Cache.{RCache, RCacheConfig}
import DefineMem.TCM.{TCM, TCMParameters}
import DefineSim.SIMCFG
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import spinal.core._
import DefineSim.SimUntils._

import scala.collection.mutable
import scala.util.Random
import DefineSim.Logger._
import spinal.core
import spinal.core._

class RCacheSim extends AnyFunSuite {

  test("test the readonly cache with Hit in white box") {
    SIMCFG(compress = true).compile {
      val config = RCacheConfig(cacheSize = (4 KiB).toInt, bytePerLine = 32, wayCount = 4, cmdDataWidth = 32, memDataWidth = 32,
        addressWidth = 32, WhiteBox = true, bankWidthReduce = true)
      val dut = new RCache(config)
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        /* generate the random address and fill the cache line test if hits valid */

        def init() = {
          dut.io.flush #= false
          dut.io.driver.cmd.valid #= false
          dut.clockDomain.waitSampling()
        }

        def fillCache(addr: BigInt, data: BigInt) = {
          dut.io.flush #= false
          dut.io.driver.cmd.valid #= false
          dut.whiteBox.writeDebug.writeEnable #= true
          dut.whiteBox.writeDebug.addr #= addr
          val line = getBitsValueInRange(addr, 5 to 9, 32)
          val tag = getBitsValueInRange(addr, 10 to 31, 32)
          val bankIndex = getBitsValueInRange(addr, 2 to 9, 32)
          val way = 0
          dut.banks(way).setBigInt(bankIndex.toLong, data)

          println(s"[ref] way : ${way} ,  line : ${line}  , bankIndex : ${bankIndex}  tag :  ${tag}   data : ${data}")
          dut.clockDomain.waitSampling()
          dut.whiteBox.writeDebug.writeEnable #= false
          dut.clockDomain.waitSampling()
        }


        def hitIt(iter: Int = 10) = {
          for (idx <- 0 until iter) {
            val addr = 0x10000000 + Random.nextInt(4096) * 4
            val data = Random.nextInt(100000)
            fillCache(addr, data)

            dut.io.flush #= false
            dut.io.driver.cmd.valid #= true
            dut.io.driver.cmd.physicalAddress #= addr
            dut.clockDomain.waitSamplingWhere(dut.io.driver.rsp.valid.toBoolean)
            println("[dut] line : " + dut.whiteBox.lineIndex.toBigInt + "   bankIndex : " +
              dut.whiteBox.bankIndex.toBigInt + " tag :  " + dut.whiteBox.tagValue.toBigInt + "   rsp value " + dut.io.driver.rsp.payload.data.toBigInt)
            assert(dut.io.driver.rsp.payload.data.toBigInt == data)
          }
        }

        init()
        hitIt(1000)
    }
  }

  test("flush and refill the cache without preset flush"){
    SIMCFG(compress = true).compile {
      val config = RCacheConfig(cacheSize = (32 KiB).toInt, bytePerLine = 32, wayCount = 4, cmdDataWidth = 32, memDataWidth = 32,
        addressWidth = 32, WhiteBox = true, bankWidthReduce = true,preResetFlush = false)
      val dut = new RCache(config)
      dut
  }.doSimUntilVoid{
    dut =>
      dut.clockDomain.forkStimulus(10)
      dut.io.driver.cmd.valid #= false
      dut.io.flush #= true
      dut.clockDomain.waitSamplingWhere(dut.lineLoader.flushArea.flushDone.toBoolean)
      for(idx <- 0 until 256){
        assert(dut.ways(Random.nextInt(4)).tags.getBigInt(idx) == 0)
      }
      simSuccess()
    }
  }

}
