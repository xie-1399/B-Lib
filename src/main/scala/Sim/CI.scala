package lib.Sim

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

object VCS{
  val flags = VCSFlags(
    compileFlags = List("-kdb","-cpp g++-4.8","-cc gcc-4.8", "+define+UNIT_DELAY", "+define+no_warning"),
    elaborateFlags = List("-kdb","-lca","-cpp g++-4.8","-cc gcc-4.8","-LDFLAGS -Wl,--no-as-needed")
  )
  val simCfg = SimConfig
    .withVCS(flags)
    .withFSDBWave
    .workspacePath("simulation")
}

object SIMCFG{
  def apply(gtkFirst:Boolean = false): SpinalSimConfig = {
    sys.env.get("VCS_HOME") match {
      case Some(_) => if(gtkFirst) CI.simCfg else VCS.simCfg
      case None => CI.simCfg
    }
  }
}
