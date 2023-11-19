package DefineMem.Cache

import spinal.core._
import spinal.lib._

/* here set about read-only bundle */
case class RCacheMemCmd(p:RCacheConfig) extends Bundle{
  import p._
  val address = UInt(addressWidth bits)
  val size = UInt(log2Up(log2Up(p.bytePerLine) + 1) bits) // Todo with size
}

case class RCacheMemRsp(p:RCacheConfig) extends Bundle{
  import p._
  val data = Bits(p.memDataWidth bits)
  val error = Bool()
}

case class RCacheBus(p:RCacheConfig) extends Bundle with IMasterSlave{
  val cmd = Stream(RCacheMemCmd(p))
  val rsp = Flow(RCacheMemRsp(p))
  override def asMaster() = {
    master(cmd)
    slave(rsp)
  }
  def toAxi4ReadOnly() = {}
}

/* the flush Bus will flush the banks with cache set */
case class FlushBus(p:RCacheConfig) extends Bundle with IMasterSlave{
  val cmd = Event
  val rsp = Bool()
  override def asMaster(): Unit = {
    master(cmd)
    in(rsp)
  }
}

/* the read cmd driver the cache */
case class RCacheDriverCmd(p:RCacheConfig) extends Bundle{
  val physicalAddress = UInt(p.addressWidth bits)
  val byPass = ifGen(p.byPass) {Bool()}
}

case class RCacheDriverRsp(p:RCacheConfig) extends Bundle{
  val cacheMiss = Bool()
  val error = Bool()
  val data = Bits(p.cmdDataWidth bits)
}

case class RCacheDriverBus(p:RCacheConfig) extends Bundle with IMasterSlave{
  val cmd = Stream(RCacheDriverCmd(p))
  val rsp = Flow(RCacheDriverRsp(p))

  override def asMaster(): Unit = {
    master(cmd)
    slave(rsp)
  }
}