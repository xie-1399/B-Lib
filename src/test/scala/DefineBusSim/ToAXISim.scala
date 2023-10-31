package DefineBusSim

import DefineBus.Axi.{AxiInit, BusParameters, ToAxi4}
import DefineSim.SIMCFG
import DefineSim.SpinalSim.ScoreBoardSimulation
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import spinal.lib.bus.amba4.axi.sim.{AxiMemorySim, AxiMemorySimConfig}
import spinal.lib.sim.{ScoreboardInOrder, StreamDriver, StreamMonitor}

import scala.collection.mutable
import scala.util.Random


class ToAXISim extends AnyFunSuite {

    def compare(ref: Array[Byte], dut: Array[Byte]): Boolean = {
      ref.deep == dut.deep
  }

  test("read cmd to axi") {
    SIMCFG(compress = true).compile {
      val parameters = BusParameters()
      val dut = new ToAxi4(parameters)
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        /* send the read cmd */
        def init() = {
          AxiInit(dut.io.bus) /* initial the master axi request */
          dut.io.writecmd.valid #= false
          dut.io.readcmd.valid #= false
          dut.io.readrsp.ready #= true
          dut.clockDomain.waitSampling()
        }
        init()
        var fire = 0
        val memSim = AxiMemorySim(dut.io.bus,clockDomain = dut.clockDomain,config = AxiMemorySimConfig())
        val memory = memSim.memory
        // val order = (0 until 4096).map(_.toByte).toArray
        val randomByteArray:Array[Byte] = Array.fill(4096)(Random.nextInt(128).toByte)
        memory.writeArray(0x80000000l,randomByteArray)
        memSim.start()
        val queue = mutable.Queue[Long]()
        val dutArray = mutable.ArrayBuffer[Byte]()

        StreamDriver(dut.io.readcmd,dut.clockDomain){
            payload =>
              val randomAddress = 0x80000000l + 8 * Random.nextInt(32)
              /* the Axi Memory sim has the alignedBurstAddress -> so the address must with word align */
              payload.address #= randomAddress
              queue.enqueue(randomAddress)
              true
          }
        StreamMonitor(dut.io.readrsp,dut.clockDomain){
            payload =>
              dutArray ++= payload.data.toBytes
              if(dutArray.length == 8 * 8){
                val refAddress = queue.dequeue()
                val ref = memory.readArray(refAddress,64) /* get the reference value */
                println("ref:" + ref.mkString(","))
                println("dut:" + dutArray.mkString(","))
                assert(compare(ref,dutArray.toArray))
                fire += 1
                dutArray.clear()
              }
          }
        dut.clockDomain.waitSamplingWhere(fire == 10000)
    }
  }


  test("write cmd to axi"){
    /* notice the fragment in the spinal*/
    SIMCFG(compress = true).compile {
      val parameters = BusParameters()
      val dut = new ToAxi4(parameters)
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        var fire = 0

        def init() = {
          AxiInit(dut.io.bus) /* initial the master axi request */
          dut.io.writecmd.valid #= false
          dut.io.writecmd.last #= false
          dut.io.readcmd.valid #= false
          dut.io.readrsp.ready #= true
          dut.clockDomain.waitSampling()
        }
        init()
        val memSim = AxiMemorySim(dut.io.bus, clockDomain = dut.clockDomain, config = AxiMemorySimConfig())
        val memory = memSim.memory
        val randomByteArray: Array[Byte] = Array.fill(4096)(0.toByte)
        memory.writeArray(0x80000000l, randomByteArray)
        memSim.start()

        def monitor() = {
          dut.clockDomain.onSamplings{
            dut.io.writecmd.last #= false
            if (dut.io.bus.w.valid.toBoolean && dut.io.bus.w.ready.toBoolean) {
              fire += 1
            }
            if (fire == 7) {
              dut.io.writecmd.last #= true
              fire = 0
            }
          }
            dut.io.writecmd.valid #= true
            dut.io.writecmd.payload.address #= 0x80000000l
            dut.io.writecmd.last #= false
            dut.io.writecmd.payload.data #= 0x11
            dut.clockDomain.waitSamplingWhere{
              dut.io.writersp.valid.toBoolean
            }
        }
        monitor()
        val ref = memory.readArray(0x80000000l, 64) /* get the reference value */
        println("ref:" + ref.mkString(","))
    }
  }
}