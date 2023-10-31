package DefineBus

import DefineBus.Apb3.APBOperation
import DefineSim.SpinalSim._
import DefineSim._
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb._

/*
  * the bus slave factory is used widely in spinal lib -> so get the use of it
  * document : https://spinalhdl.github.io/SpinalDoc-RTD/v1.8.0/SpinalHDL/Developers%20area/bus_slave_factory_impl.html#busslavefactory
*/

class BusSlaveFactorySim extends PrefixComponent{
  /* show use the apb slave to read some and driver something */
  val io = new Bundle{
    val apb = slave(APBOperation.create(config = Apb3Config(addressWidth = 16,dataWidth = 32)))
  }

  val CMP = 0x4000
  val CMP_ = 0x4010
  val TIME = 0xFF86
  val OFFSET = 0x3612

  val time = Reg(UInt(64 bits)).init(0)
  val cmp = Reg(UInt(64 bits)).init(0)
  val value = Reg(UInt(32 bits)).init(0)
  val offset = Reg(UInt(16 bits))

  val interrupt = RegNext(time >= cmp)
  val stop = False

  when(!stop){
    time := time + 1
  }

  val factory = Apb3SlaveFactory(io.apb)
  println("bus DataWidth: " + factory.busDataWidth)

  /* use the factory create regs like this */
  factory.createReadAndWrite(value,CMP_)
  factory.write(offset,OFFSET,bitOffset = 8) /* write using bit offset from 8 -> 24 */
  factory.read(offset,OFFSET) /* the read bit set is not use very often */
  /* do something when read or write happens */
  factory.onRead(TIME){}
  factory.onWrite(CMP){}

  /* write some values to the address */
  factory.writeMultiWord(cmp,CMP)

  /* read word from the reg address */
  factory.readMultiWord(time,TIME)

}

object BusSlaveFactorySim extends App{
  import spinal.core.sim._
  SIMCFG(gtkFirst = true).compile{
    val dut = new BusSlaveFactorySim()
    dut
  }.doSim{
    dut =>
      dut.clockDomain.forkStimulus(10)
      val driver = APBOperation.sim(dut.io.apb,dut.clockDomain)
      driver.write(0x4000,0x100)

      dut.clockDomain.waitSampling(100)

      val readValue = driver.read(0xFF86)
      println("time is " + readValue)

      driver.write(0x4010,19)
      val cmp_ = driver.read(0x4010)
      println("cmp _ is " + cmp_)

      driver.write(0x3612,0x00001100)
      val offset = driver.read(0x3612)
      println("offset is " + offset)

  }

}
