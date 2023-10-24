package DefineUntils.Counter

/* this play show a more clearly usage of counter and it's sequence */
import DefineSim.SIMCFG
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._
import scala.collection.mutable._
import scala.util.Random


class CounterPlay extends PrefixComponent{

  val io = new Bundle{
    val tick = in Bool()
    val willoverflow = out Bool()
    val willoverflowIfInc = out Bool()
    val value = out UInt(5 bits)
  }

  val play = False.allowOverride()
  val play2 = RegInit(False).allowOverride()

  val counter = Counter(32).init(0)
  when(io.tick){
    play := True
    play2 := True /* assign one cycle late and keep value */
    counter.increment()
  }
  io.value := counter.value
  io.willoverflow := counter.willOverflow
  io.willoverflowIfInc := counter.willOverflowIfInc

}

object removeAssignPlay{
  /* this show about how to remove some signals in the component */
  def apply(): CounterPlay = {
    val play = new CounterPlay()
    play.play.removeAssignments()
    play.play2.removeAssignments()
    play
  }
}

object CounterPlaySim extends App{
  import spinal.core.sim._
  SIMCFG(compress = true).compile{
    val dut = new CounterPlay()
    dut.counter.willOverflow.simPublic()
    dut.counter.value.simPublic()
    dut
  }.doSimUntilVoid {
    dut =>
      dut.clockDomain.forkStimulus(10)
      val thread = fork{
        while(true){
          dut.io.tick #= Random.nextInt(10) > 5
          if(dut.counter.willOverflow.toBoolean){
            simSuccess()
          }
          dut.clockDomain.waitSampling()
          println(dut.counter.value.toLong)
        }
      }
  }
}


object CounterAssign extends App{
  val rtl = new RtlConfig().GenRTL(removeAssignPlay())
}