package DefineDevice.Interrupt

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib.bus.amba4.axilite._
import spinal.lib.bus.amba4.axilite._
import spinal.lib.misc.AxiLite4Clint
import spinal.lib.misc.plic._
import spinal.lib._

/* the platform-level interrupt controller -> control all external interrupt to the target
  * the material : https://github.com/riscv/riscv-plic-spec/blob/master/riscv-plic.adoc
  * should know the material first*/
object PLic {

  /* all the source interrupt first goto the gateway*/

  /* the plic mapping address reg */
  def sifiveMapper = PlicMapping(
    gatewayPriorityOffset = 0x0000,
    gatewayPendingOffset = 0x1000,
    targetEnableOffset = 0x2000,
    targetThresholdOffset = 0x200000,
    targetClaimOffset = 0x200004,
    gatewayPriorityShift = 2,
    targetThresholdShift = 12,
    targetClaimShift = 12,
    targetEnableShift = 7,
    gatewayPriorityReadGen = true,
    gatewayPendingReadGen = true,
    targetThresholdReadGen = true,
    targetEnableReadGen = true
  )

  class plicDemo extends PrefixComponent {
    val io = new Bundle{
      val axilite4 = slave (AxiLite4(AxiLite4Config(addressWidth = 16,dataWidth = 32)))
      val source = in Bits(4 bits)
      val target = out Bits(1 bits)
    }
    /* the source interrupt send to the target */
    val axiLite4Plic = new AxiLite4Plic(sourceCount = 4,targetCount = 1)
    axiLite4Plic.io.bus <> io.axilite4
    axiLite4Plic.io.sources <> io.source
    axiLite4Plic.io.targets <> io.target

    /* use the axi lite4 to drive it simulation see the simulation about it*/
  }

}

