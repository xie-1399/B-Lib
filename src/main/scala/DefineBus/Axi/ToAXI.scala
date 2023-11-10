package DefineBus.Axi

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.lib._
import spinal.core._
import spinal.lib.bus.amba4.axi._
/*
 * convert the cmd and rsp request to the Axi Bus request
 * this is common request to trans cmd to the Axi4 Bus
 * test about the out of order */

case class BusParameters(
                        addressWidth:Int = 32,
                        dataWidth:Int = 64,
                        lineSize:Int = 64
                        )

case class ReadCmd(p:BusParameters) extends Bundle{
  val address = UInt(p.addressWidth bits)
}

case class ReadRsp(p:BusParameters) extends Bundle{
  val data = Bits(p.dataWidth bits)
  val error = Bool()
}

case class WriteCmd(p:BusParameters) extends Bundle{
  val address = UInt(p.addressWidth bits)
  val data = Bits(p.dataWidth bits)
}

case class WriteRsp(p:BusParameters) extends Bundle{
  val error = Bool()
}

/* the bus will read/write with burst increase */

class ToAxi4(p:BusParameters) extends PrefixComponent {

  val axiConfig = Axi4Config(
    addressWidth = p.addressWidth,
    dataWidth = p.dataWidth,
    useId = false,
    useRegion = false,
    useLock = false,
    useCache = false,
    useQos = false,
  )
  val io = new Bundle{
     val bus = master(Axi4(axiConfig))
     val readcmd = slave Stream (ReadCmd(p))
     val readrsp = master Stream (ReadRsp(p))
     val writecmd = slave Stream (Fragment(WriteCmd(p)))
     val writersp = master Flow (WriteRsp(p))
  }
  val axi = Axi4(axiConfig)

  /* read */
  axi.ar.valid := io.readcmd.valid
  axi.ar.addr := io.readcmd.address
  axi.ar.prot := B"010"
  axi.ar.len := p.lineSize * 8 / p.dataWidth - 1
  axi.ar.size := log2Up(p.dataWidth / 8)
  axi.ar.setBurstINCR()
  io.readcmd.ready := axi.ar.ready
  axi.r.ready := True
  io.readrsp.valid := axi.r.valid
  io.readrsp.data := axi.r.data
  io.readrsp.error := !axi.r.isOKAY()

  /* write */
  val (awRaw, wRaw) = StreamFork2(io.writecmd)
  val awFiltred = awRaw.throwWhen(!awRaw.first)
  val aw = awFiltred.stage()
  axi.aw.valid := aw.valid
  axi.aw.addr := aw.address
  axi.aw.prot := B"010"
  axi.aw.len := p.lineSize * 8 / p.dataWidth - 1
  axi.aw.size := log2Up(p.dataWidth / 8)
  axi.aw.setBurstINCR()
  aw.ready := axi.aw.ready

  val w = wRaw.haltWhen(awFiltred.valid)
  axi.w.valid := w.valid
  axi.w.data := w.data
  axi.w.strb.setAll()
  axi.w.last := w.last
  w.ready := axi.w.ready
  io.writersp.valid := axi.b.valid
  io.writersp.error := !axi.b.isOKAY()
  axi.b.ready := True
  axi >> io.bus
}

object ToAxi4 extends App{
  val rtl = new RtlConfig(path = "temp").GenRTL(top = new ToAxi4(BusParameters()))
}