package DefinePipeline

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib.{master, slave}
import DefineSim._


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
  }


object ConnectExample extends App {
  import spinal.core.sim._
  SIMCFG(gtkFirst = true).compile {
    val dut = new TwoStagePipe()
    dut
  }.doSim {
    dut =>
      dut.clockDomain.forkStimulus(10)
      /* simulation and think about it*/



  }

}