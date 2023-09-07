package DefineBus.SimpleBus

/*
  define a simple bus to read/write the memory
  design it as a common bus interface
*/
import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import DefineBus.Apb3.APBOperation
import spinal.lib.bus.amba3.apb._
import scala.math._

case class simpleBusConfig(dataWidth:Int,addressWidth:Int,maskWidth:Int)

case class SMB(config:simpleBusConfig) extends Bundle with IMasterSlave{
  val wdata = Bits(config.dataWidth bits)
  val rdata = Bits(config.dataWidth bits)
  val write = Bool()
  val valid = Bool()
  val ready = Bool()
  val address = UInt(config.addressWidth bits)
  val mask = Bits(config.maskWidth bits)

  override def asMaster(): Unit = {
    in(rdata,ready)
    out(wdata,write,valid,mask,address)
  }

  def getConfig() = this.config
}

/*
  The simple bus can just connect a simple ram
*/

class SMBMemory(config:simpleBusConfig,sync:Boolean = false) extends PrefixComponent{
  val io = new Bundle{
    val smb = slave (SMB(config))
  }
  val mem = Mem(Bits(config.dataWidth bits),pow(2,config.addressWidth).toInt)
  mem.write(io.smb.address,io.smb.wdata,enable = io.smb.valid && io.smb.ready && io.smb.write,mask = io.smb.mask)
  if(sync){
    io.smb.rdata := mem.readSync(io.smb.address,enable = io.smb.valid && io.smb.ready && !io.smb.write)
  }
  else {
   io.smb.rdata := mem.readAsync(io.smb.address,writeFirst)
  }
  io.smb.ready := True
}