package lib.Sim

import spinal.core._
import spinal.core.sim._

// just generate verilog or using in the test
/*
** val rtl = RtlConfig()
** rtl.setconfig(new XXX)
*/

object SpinalSim{
  case class RtlConfig(path:String = "rtl",frequency : Int = 50, hdl: SpinalMode = Verilog){
    def setconfig = SpinalConfig(mode = hdl,targetDirectory = path,defaultClockDomainFrequency = FixedFrequency(frequency MHz))
  }

  def apply() = SimConfig.withVcdWave.withConfig(
    SpinalConfig(targetDirectory = "rtl")).workspacePath("simulation")

  //only sample to produce the wave with iter cycles
  def onlySample(clockDomain: ClockDomain, operation: () => Unit, iter: Int = 100): Unit = {
    for (idx <- 0 until iter) {
      operation()
      clockDomain.waitSampling()
    }
  }

  //add tags about the bundle with the simPublic() but not about the Mem
  def addSimPublic(list: List[Data]): Unit = {
    for(elem <- list){
      elem simPublic()
    }
  }


  //no Io predix
  class PrefixComponent extends Component{
    noIoPrefix()
  }
}
