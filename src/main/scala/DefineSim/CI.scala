package DefineSim

// a way to use vcs or Verilator

import spinal.core.SpinalConfig
import spinal.core.sim.{SimConfig,SpinalSimConfig}
import spinal.sim.VCSFlags

object CI {
  val simCfg = SimConfig
    .withWave
    .withVerilator
    .workspacePath("simulation")
}

object FST{
  val simCfg = SimConfig
    .withFstWave
    .withVerilator
    .workspacePath("simulation")
}

object VCS{
  val flags = VCSFlags(
    compileFlags = List("-kdb"),
    elaborateFlags = List("-kdb","-LDFLAGS -Wl,--no-as-needed")
  )
  val simCfg = SimConfig
    .withVCS(flags)
    .withFSDBWave
    .workspacePath("simulation")
}

object SIMCFG{
  def apply(gtkFirst:Boolean = false,compress:Boolean = false): SpinalSimConfig = {
    sys.env.get("VCS_HOME") match {
      case Some(_) => {
        (gtkFirst, compress) match {
          case (true,true) => FST.simCfg
          case (false,true) => FST.simCfg
          case (true,false) => CI.simCfg
          case (false,false) => VCS.simCfg
        }
      }
      case None => CI.simCfg
    }
  }
}
