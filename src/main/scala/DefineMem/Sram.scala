package DefineMem

import spinal.core._
import spinal.lib._
import scala.math
/*
  * the SRAM module with all kinds of write or read ports
  * with a blackbox */

case class SramParameters(
                     dataWidth:Int,
                     depth:Int,
                     maskBits:Int
                     ){
  def addrWidth = log2Up(dataWidth)
  def maskWidth = if(maskBits == 0) 0 else math.ceil(dataWidth / maskBits).toInt
}

case class SramRWIO(p:SramParameters) extends Bundle with IMasterSlave {
  val en = Bool()
  val write = Bool()
  val addr = UInt(p.addrWidth bits)
  val wdata = Bits(p.dataWidth bits)
  val mask = Bits(p.maskWidth bits)
  val rdata = Bits(p.dataWidth bits)

  override def asMaster(): Unit = {
    in(rdata)
    out(en,write,wdata,mask,mask,addr)
  }
}

class SramROIO(p:SramParameters) extends Bundle with IMasterSlave{
  /* read only*/
  val en = Bool()
  val addr = UInt(p.addrWidth bits)
  val rdata = Bits(p.dataWidth bits)
  override def asMaster(): Unit = {
    in(rdata)
    out(en,addr)
  }
}

class SramWOIO(p:SramParameters) extends Bundle with IMasterSlave{
  /* write only */
  val en = Bool()
  val addr = UInt(p.addrWidth bits)
  val wdata = Bits(p.dataWidth bits)
  val mask = Bits(p.maskWidth bits)
  override def asMaster(): Unit = {
    out(en,wdata, mask, mask, addr)
  }
}

