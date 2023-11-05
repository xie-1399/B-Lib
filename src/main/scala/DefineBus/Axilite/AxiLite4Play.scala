package DefineBus.Axilite

/* the axi lite4 is used to trans data from the kernel and io
*  each time will trans one data(Non-bufferable、Non-modifiable) the len = 1*/

// Todo fix the bugs in the play

import DefineMem.MemOperation
import DefineSim.SpinalSim._
import spinal.core._
import DefineSim._
import spinal.lib._
import spinal.lib.bus.amba4.axilite
import spinal.lib.bus.amba4.axilite.AxiLite4
import spinal.lib.bus.amba4.axilite.sim.AxiLite4Driver

class AxiLite4Play extends PrefixComponent{
  /* a simple demo use the axi lite to drive the memory */
  val io = new Bundle{
    val bus = slave (AxiLite4(5,32)) /* notice that: the axi lite4 dataWidth is only support 32/64 */
  }

  val mem = MemOperation(Bits(32 bits),32)
  val addr = RegNextWhen(io.bus.aw.addr,io.bus.aw.fire)
  io.bus.aw.ready := True
  io.bus.w.ready := True

  io.bus.b.valid := RegNext(io.bus.w.fire)
  io.bus.b.payload.resp := B"00"

  io.bus.ar.ready := True
  io.bus.r.payload.data := mem.readSync(io.bus.ar.addr,enable = io.bus.ar.fire)
  io.bus.r.valid := RegNext(io.bus.ar.fire)
  io.bus.r.resp := B"00"
  when(io.bus.w.fire){
    mem.write(addr,io.bus.w.data)
  }

}

object AxiLite4Play extends App{
  import spinal.core.sim._
  SIMCFG(gtkFirst = true).compile {
    val dut = new AxiLite4Play()
    dut.mem.simPublic()
    dut
  }.doSim {
    dut =>
      dut.clockDomain.forkStimulus(10)
      val driver = AxiLite4Driver(dut.io.bus,dut.clockDomain)
      driver.reset()
      dut.clockDomain.waitSampling()

      dut.mem.setBigInt(3,0x10001000)
      val readValue = driver.read(3)
      assert(readValue == 0x10001000)

      driver.write(3,0x20002000)
      println("write the mem value :"+ driver.read(3).toLong.toHexString)
  }
}