package DefinePipeline

import DefineSim.SIMCFG
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}

import scala.collection.mutable
import scala.util.Random
/*
* the module is from the https://github.com/ucb-bar/gemmini/blob/master/src/main/scala/gemmini/Pipeline.scala
* it's seems useful if want to pass a signal bundle with pipeline
* */

import spinal.core._
import spinal.lib._

class Pipeline[T <: Data](gen:T,latency:Int)(comb:Seq[T => T] = Seq.fill(latency + 1)((x:T) => x)) extends PrefixComponent{

  val io = new Bundle{
    val input = slave Stream(gen)
    val output = master Stream(gen)
    val busy = out Bool()
  }

  if(latency == 1){ /* without pipe going connect from it */
    io.output.arbitrationFrom(io.input)
    io.output.payload := comb.head(io.input.payload)
    io.busy := io.input.valid
  }
  else {
    val stages = Vec(Reg(gen), latency)
    val valids = Vec(RegInit(False), latency)
    val stalling = Vec(Bool(), latency)
    io.busy := io.input.valid || valids.reduce(_ || _)

    io.input.ready := !stalling.head
    stalling.last := valids.last && !io.output.ready
    (stalling.init, stalling.tail, valids.init).zipped.foreach { case (s1, s2, v1) =>
      s1 := v1 && s2
    }

    //Valid signals
    io.output.valid := valids.last
    when(io.output.ready) {
      valids.last := False
    }
    (valids.init, stalling.tail).zipped.foreach { case (v1, s2) =>
      when(!s2) {
        v1 := False
      }
    }

    // When the pipeline stage behind you is valid then become true
    when(io.input.fire) {
      valids.head := True
    }
    (valids.tail, valids.init).zipped.foreach { case (v2, v1) =>
      when(v1) {
        v2 := True
      }
    }

    // Stages
    when(io.input.fire) {
      stages.head := comb.head(io.input.payload)
    }
    io.output.payload := comb.last(stages.last)
    ((stages.tail zip stages.init) zip (stalling.tail zip comb.tail.init)).foreach { case ((st2, st1), (s2, c1)) =>
      when(!s2) {
        st2 := c1(st1)
      }
    }
  }
}

object Pipeline {
  def apply[T <: Data](input: Stream[T], latency: Int, comb: Seq[T => T]): Stream[T] = {
    val p = new Pipeline(cloneOf(input.payload), latency)(comb)
    p.io.input <> input
    p.io.output
  }

  def apply[T <: Data](input: Stream[T], latency: Int): Stream[T] = {
    val p = new Pipeline(cloneOf(input.payload), latency)()
    p.io.input <> input
    p.io.output
  }
}



object PipelineGo extends App{
  import spinal.core.sim._
  SIMCFG(compress = true).compile{
   val dut = new Pipeline(UInt(5 bits),latency = 2)()
   dut
  }.doSimUntilVoid{
    dut =>
      dut.clockDomain.forkStimulus(10)
      val maxCycles = 1000
      val inputs = Seq.fill(1000)(Random.nextInt(32))
      val queue = mutable.Queue[BigInt]()
      def init() = {
        dut.io.input.valid #= false
        dut.io.input.ready #= false
        dut.clockDomain.waitSampling()
      }
      init()
      val thread = fork{
        for(idx <- 0 until maxCycles){
          dut.io.input.valid.randomize()
          dut.io.output.ready.randomize()
          dut.io.input.payload #= inputs(idx)
          dut.clockDomain.waitSampling()
          val inputFire = dut.io.input.valid.toBoolean && dut.io.input.ready.toBoolean
          val outputFire = dut.io.output.valid.toBoolean && dut.io.output.ready.toBoolean
          if(inputFire){
            queue.enqueue(dut.io.input.payload.toBigInt)
          }
          if(outputFire){
            assert(queue.dequeue() == dut.io.output.payload.toBigInt)
          }
        }
      }
      thread.join()
      simSuccess()
  }

}