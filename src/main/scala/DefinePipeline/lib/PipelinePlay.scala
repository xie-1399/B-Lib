package DefinePipeline.lib

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import spinal.lib.pipeline.Connection._
import spinal.lib.pipeline._
  /*
   use the pipeline lib examples and think about it
   more function should look the source code of the pipeline lib
   here is the three simple examples about pipeline
  */

  /*
  insert regs in pipeline and use the stage reg
  */
class ExampleOne extends PrefixComponent{
  val a, b = in UInt (8 bits)
  val result = out UInt (8 bits)

  implicit val pip = new Pipeline
  val stageA = new Stage()(pip)  //implicit use the pipeline
  val stageB = new Stage(connection = M2S())
  val stageC = new Stage(connection = M2S())

  val SUM = stageA.insert(a + b)
  result := stageC(SUM)

  pip.build()
}

  /*
  in each stage can use stageable to check about it
 */

class ExampleTwo extends PrefixComponent{
  val a, b = in UInt (8 bits)
  val result = out Bool()

  val pip = new Pipeline {
    val SUM = Stageable(UInt(8 bits))
    val CHECK = Stageable(Bool())

    val stageA = new Stage {
      SUM := a + b
    }
    val stageB = new Stage(connection = M2S()) {
      CHECK := SUM < 5
    }
    val stageC = new Stage(connection = M2S()) {
      result := CHECK
    }
    val check = stageC(CHECK)
  }
  pip.build()
}

class ExampleThree extends PrefixComponent{
  val io = new Bundle {
    val source = slave Stream (UInt(8 bits))
    val sink = master Flow (UInt(8 bits))

    val cond0 = in UInt (8 bits)
  }

  val pipeline = new Pipeline {
    val s0, s1, s2 = new Stage()
    val A, B, C = Stageable(UInt(8 bits))

    import Connection._

    connect(s0, s1)(M2S())
    connect(s1, s2)(M2S())

    val onS0 = new Area {

      import s0._

      valid := io.source.valid
      io.source.ready := s0.isReady
      A := io.source.payload
    }

    val onS1 = new Area {
      import s1._
      when(io.cond0 === 0) {
        haltIt()
      }
      when(io.cond0 === 2) {
        flushNext()
      }

      B := A + 1
      s1(C, "miaou") := A + 2
      (C, "wuff") := A + 3
      (C, "wuff2") := A + 4

      {
        implicit val offset = new StageableOffset("yololo")
        C := U(5)
      }
    }

    val onS2 = new Area {
      import s2._
      io.sink.valid := internals.output.valid
      io.sink.payload := B + s2(C, "miaou") + s2(C, "wuff") + (C, "wuff2") + (C, "yololo")
    }

  }
  pipeline.build()

}
