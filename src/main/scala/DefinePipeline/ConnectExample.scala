package DefinePipeline

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib.{master, slave}
import DefineSim._

import scala.collection.mutable
import scala.util.Random


  case class request() extends Bundle{
    val testA = Bool()
    val testB = UInt(3 bits)
    val testC = Bits(4 bits)
  }

  class TwoStagePipe() extends PrefixComponent{
    val io = new Bundle{
      val stage1 = slave Stream request()
      val stage2 = master Stream request()
      val halt = in Bool()
      val flush = in Bool()
      val rightOutfire = in Bool()
    }

    val pipeline = PipelineConnect(io.stage1,io.stage2,io.rightOutfire,io.flush,io.halt)
    when(io.stage1.fire) {
      io.stage2.testA := !io.stage1.testA
      io.stage2.testB := io.stage1.testB + 1
      io.stage2.testC := io.stage2.testB.asBits.resized
    }
  }


/* the res will be show in the wave about the build pipeline  */
object ConnectExample extends App {
  import spinal.core.sim._
  SIMCFG(gtkFirst = true).compile {
    val dut = new TwoStagePipe()
    dut
  }.doSimUntilVoid {
    dut =>
      dut.clockDomain.forkStimulus(10)
      val queue = new mutable.Queue[BigInt]()
      /* simulation and think about it*/
      def pipeGo() = {
        dut.io.stage1.valid #= true
        dut.io.halt #= false
        dut.io.flush #= false
        dut.io.rightOutfire #= true
        dut.io.stage1.payload.testA.randomize()
        dut.io.stage1.payload.testB #= Random.nextInt(4)
        dut.io.stage1.payload.testC #= Random.nextInt(6)
        dut.io.stage2.ready #= true
        dut.clockDomain.waitSampling()
      }

      def haltIt() = {
        dut.io.stage1.valid.randomize()
        dut.io.halt #= true
        dut.io.flush #= false
        dut.io.rightOutfire #= true
        dut.io.stage1.payload.testA.randomize()
        dut.io.stage1.payload.testB #= Random.nextInt(4)
        dut.io.stage1.payload.testC #= Random.nextInt(6)
        dut.io.stage2.ready #= true
        dut.clockDomain.waitSampling()
      }

      def flushIt() = {
        dut.io.stage1.valid.randomize()
        dut.io.halt #= false
        dut.io.flush #= true
        dut.io.rightOutfire #= true
        dut.io.stage1.payload.testA.randomize()
        dut.io.stage1.payload.testB #= Random.nextInt(4)
        dut.io.stage1.payload.testC #= Random.nextInt(6)
        dut.io.stage2.ready #= true
        dut.clockDomain.waitSampling()
      }

      val thread = fork{
        for(idx <- 0 until 20){
          pipeGo()
        }
        for(idx <- 0 until 200){
          val random = Random.nextInt(50)
          if(random < 5){
            flushIt()
          }
          else if(random > 40){
            haltIt()
          }
          else {
            pipeGo()
          }
        }
      }
      simSuccess()
  }

}