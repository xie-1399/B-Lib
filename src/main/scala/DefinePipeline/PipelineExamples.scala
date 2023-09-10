package DefinePipeline

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import spinal.lib.pipeline._
/*
 use the pipeline lib examples and think about it
 more function should look the source code of the pipeline lib
*/

class PipelinewithPC extends PrefixComponent{
  val io = new Bundle{
    val halt = in Bool()
    val flush = in Bool()
  }

  val pipeline = new Pipeline{ //create the pipeline
    val s0,s1,s2 = new Stage()
    val PC = Stageable(UInt(32 bits))
    val flush = Stageable(Bool())
    val halt = Stageable(Bool())

    //connect the stage
    import Connection._
    connect(s0,s1)(M2S())
    connect(s1,s2)(M2S())
    val resetValue = Reg(UInt(32 bits)) init 0x80000000l

    val S0 = new Area {
      import s0._  //should import with it
      PC := resetValue
      resetValue := resetValue + 4
      s0.haltIt(io.halt)
      s1.flushIt(io.flush)
    }

    val S1 = new Area {
      import s1._
      val valid = internals.output.valid
    }

  }

  pipeline.build()
}