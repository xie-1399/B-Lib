package DefineSim

import spinal.core._
import spinal.core.sim._

// just generate verilog or using in the test
/* two ways to generate the verilog
** val rtl = new RtlConfig()
** rtl.setconfig(new XXX)
*
** val rtl = new RtlConfig().GenRTL(top = new XXX())
*/

object SpinalSim{

  class RtlConfig(path:String = "rtl",frequency : Int = 50, hdl: SpinalMode = Verilog){
    def setconfig = SpinalConfig(mode = hdl,targetDirectory = path,defaultClockDomainFrequency = FixedFrequency(frequency MHz))

    def GenRTL[T <: Component](top: => T, config: SpinalConfig = SpinalConfig(targetDirectory = path), pruned: Boolean = true) = {
      if (pruned) SpinalVerilog(config = config)(top).printPruned() else SpinalVerilog(config)(top)
    }

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

  //add the data type
  def addSimPublic(list: List[Data]): Unit = {
    for(elem <- list){
        elem simPublic()
    }
  }

  //no Io predix
  class PrefixComponent() extends Component{
    noIoPrefix()
  }

  // set the generator signal name(for area use the Composite)
  def DataName[T <: Data](dataType: T, name: String) = {
    dataType.setName(name)
  }
}