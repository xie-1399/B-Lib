package DefineMem.TCM

import spinal.core._
import spinal.lib._

case class TCMCmd(p:TCMParameters) extends Bundle{
  val address = UInt(p.physicalWidth bits)
  val io = in Bool()
  val wr = ifGen(!p.readOnly){Bool()}
  val data = ifGen(!p.readOnly){Bits(p.dataWidth bits)}
  val mask = ifGen(!p.readOnly){Bits(p.maskWidth bits)}
}

case class TCMRsp(p:TCMParameters) extends Bundle{
  val data = Bits(p.dataWidth bits)
  val error = Bool()
}

case class TCMRequest(p:TCMParameters) extends Bundle with IMasterSlave{
  val cmd = Stream (TCMCmd(p))
  val rsp = Stream (TCMRsp(p))

  override def asMaster(): Unit = {
    master(cmd)
    slave(rsp)
  }
}