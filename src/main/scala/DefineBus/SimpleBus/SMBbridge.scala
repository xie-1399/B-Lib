package DefineBus.SimpleBus


import spinal.lib._
import DefineBus.Apb3.APBOperation
import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib.bus.amba3.apb.{Apb3, Apb3Config}


/*
 Todo test
 this show about how to convert the simpleBus cmd to other bus
*/

class SimpleBusToAPB(config:simpleBusConfig) extends PrefixComponent {
  val io = new Bundle{
    val cmd = slave(SMB(config))
    val apb = master(Apb3(config.addressWidth,config.dataWidth))
  }
  val apbConfig = Apb3Config(addressWidth = config.addressWidth,dataWidth = config.dataWidth)

  //convert it to the apb
  def toApb():Apb3 = {
    val apb = APBOperation.create(apbConfig)
    apb.PADDR := io.cmd.address
    apb.PWRITE := io.cmd.write
    apb.PWDATA := io.cmd.wdata
    apb.PSEL := io.cmd.valid.asBits
    apb.PENABLE := RegNext(io.cmd.valid).clearWhen(io.cmd.ready)
    io.cmd.rdata := apb.PRDATA
    io.cmd.ready := apb.PREADY && apb.PENABLE
    apb
  }
  APBOperation.connect(toApb(),io.apb,false) // without m2s
}