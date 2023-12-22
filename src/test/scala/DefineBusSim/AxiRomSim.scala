package DefineBusSim

import DefineSim.SIMCFG
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import DefineBus.Axi._
import spinal.core
import spinal.core._

import scala.util.Random


class AxiRomSim extends AnyFunSuite{

  test("read it"){

    SIMCFG(compress = true).compile{
      val dut = new AxiRom(32,16 KiB,4,WhiteBox = true)
      dut
    }.doSimUntilVoid{
      dut =>
        dut.clockDomain.forkStimulus(10)
        dut.io.axi.ar.valid #= false
        dut.clockDomain.waitSampling()

        val readIt = fork{
          for(idx <- 0 until 1024){
            val addr = Random.nextInt(1024 * 4 / 32) * 4

            dut.io.axi.ar.valid #= true
            dut.io.axi.r.ready #= true
            dut.io.axi.ar.addr #= addr
            dut.io.axi.ar.id #= 0
            dut.clockDomain.waitSampling()
            dut.io.axi.ar.valid #= false
            dut.clockDomain.waitSamplingWhere(dut.io.axi.r.valid.toBoolean)
            assert(dut.io.axi.r.payload.data.toBigInt == addr / 4)
          }
          simSuccess()
        }

        readIt.join()
        simSuccess()
    }

  }

}
