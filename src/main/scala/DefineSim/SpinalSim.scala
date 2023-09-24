package DefineSim

import spinal.core._
import spinal.core.sim._
import spinal.lib.sim._
import scala.collection.mutable.ArrayBuffer

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

  /* only sample to produce the wave with iter cycles */
  def onlySample(clockDomain: ClockDomain, operation: () => Unit = null, iter: Int = 100): Unit = {
    for (idx <- 0 until iter) {
      operation()
      clockDomain.waitSampling()
    }
  }

  /*add the data type and support the memory sim public*/
  def addSimPublic[T <: Data](list: List[Data] = null,mem:Mem[T] = null): Unit = {
    if(list != null) for (elem <- list) {
      elem simPublic()
    }
    if(mem != null) mem simPublic()
  }

  /* driver the sim data with init value like false for Bool*/
  def simInitValue[T<:Data](list:ArrayBuffer[T],boolean: Boolean,bits:Boolean,
                            boolValue:Boolean = false,bitsValue:Int = 0,clockDomain: ClockDomain = null) = {
    if(boolean) for(data <- list) {
      data.asInstanceOf[Bool] #= boolValue
    }
    if(bits) for(data <- list){
      data.asInstanceOf[BitVector] #= bitsValue
    }
    if(clockDomain != null) clockDomain.waitSampling()
  }


  /*no Io prefix and support define the component name */
  class PrefixComponent(name:String = null) extends Component{
    noIoPrefix()
    if(name != null) setDefinitionName(name)
  }

  /* use the score board to test the simulation data is right*/
  def ScoreBoardSimulation[T]() = {
    /* get a new board (pushDut and pushRef) */
    val scoreboardInOrder = ScoreboardInOrder[T]()
    scoreboardInOrder
  }

  /* set the generator signal name(for area use the Composite) */
  def DataName[T <: Data](dataType: T, name: String) = {
    dataType.setName(name)
  }
}