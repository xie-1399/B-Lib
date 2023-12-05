package DefineUntilsSim

import DefineSim.SIMCFG
import DefineSim.SpinalSim.PrefixComponent
import DefineUntils.StreamPlayer
import org.scalatest.funsuite.AnyFunSuite
import spinal.core._
import spinal.core.sim._

class threadFullPlay extends PrefixComponent{
  val io = new Bundle{
    val a = in Bits(8 bits)
    val b = in Bits(8 bits)
    val c = out Bits(8 bits)
  }

  val tmp = Reg(Bits(8 bits)).init(0)
  tmp := io.a & io.b
  io.c := tmp
}


class threadFullPlaySim extends AnyFunSuite {
  // so the sleep will show in the sim time with halt
  test("thread sleep") {
    SIMCFG(compress = true).compile {
      val dut = new threadFullPlay()
      dut
    }.doSimUntilVoid {
      dut =>
        dut.clockDomain.forkStimulus(10)
        var iter = 0
        val threadA = fork{
            while (true){
              dut.io.a.randomize()
              dut.io.b.randomize()
              if(iter == 10){println(s"${simTime()} loop is done");simSuccess()}
              dut.clockDomain.waitSampling()
              sleep(1000)
              iter += 1
            }
          }
        threadA.join()
        simSuccess()
    }
  }
}