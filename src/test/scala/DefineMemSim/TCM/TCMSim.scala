package DefineMemSim.TCM

import DefineSim.SIMCFG
import DefineSim.SpinalSim._
import org.scalatest.funsuite.AnyFunSuite
import scala.util.Random
import DefineMem.TCM._
import spinal.core.sim._
import DefineSim.Logger._


/* at beginning time there will be some init value in the memory */
// Todo add a function tool to show the present the mask of the scala value

class TCMSim extends AnyFunSuite {

  test("using the read/write tcm") {
    SIMCFG(compress = true).compile {
      val p = TCMParameters(maskWidth = 4,useFlush = true)
      val dut = new TCM(p)
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        val scoreboardInOrder = ScoreBoardSimulation[BigInt]()
        dut.io.request.cmd.valid #= false
        dut.io.flush #= false
        dut.clockDomain.waitSampling()

        def setValue(withMask:Boolean = false) = {
          for(idx <- 0 until dut.mem.wordCount){
            val random = Random.nextInt(100000)
            if(!withMask)scoreboardInOrder.pushRef(BigInt(random))
            if(withMask)dut.mem.setBigInt(idx,0) else dut.mem.setBigInt(idx,random)
          }
          dut.clockDomain.waitSampling()
        }

        def writewithMask() = {
          for (idx <- 0 until dut.mem.wordCount) {

            val random = math.pow(2,Random.nextInt(60)).toLong
            val maskList = List(0,15)
            val mask = maskList(Random.nextInt(2))
            mask match {
              case 15 => scoreboardInOrder.pushRef(BigInt(random))
              case 0 => scoreboardInOrder.pushRef(BigInt(0))
              case _ =>
            }
            dut.io.request.cmd.valid #= true
            dut.io.flush #= false
            dut.io.request.cmd.io #= true
            dut.io.request.cmd.address #= idx
            dut.io.request.cmd.mask #= mask
            dut.io.request.cmd.data #= random
            dut.io.request.cmd.wr #= true
            dut.io.request.rsp.ready.randomize()
            dut.clockDomain.waitSampling()
          }
        }

        def readingInOrder() = {
          dut.clockDomain.onSamplings{
            if(dut.io.request.rsp.valid.toBoolean){
              scoreboardInOrder.pushDut(dut.io.request.rsp.data.toBigInt)
            }
          }
          for(idx <- 0 until dut.mem.wordCount){
            dut.io.request.cmd.valid #= true
            dut.io.flush #= false
            dut.io.request.cmd.io #= true
            dut.io.request.cmd.address #= idx
            dut.io.request.cmd.wr #= false
            dut.io.request.rsp.ready.randomize()
            dut.clockDomain.waitSampling()
          }
          simSuccess()
        }
        setValue(withMask = true)
        writewithMask()
        readingInOrder()
    }
  }
}
