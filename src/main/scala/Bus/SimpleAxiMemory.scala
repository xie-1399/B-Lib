package lib.Bus

import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._
import lib.Sim.SpinalSim._

//simple memory to test axi driver
class SimpleAxiMemory(axi4Config: Axi4Config) extends Component {
  val io = new Bundle{
    val bus = slave(Axi4(axi4Config))
  }
  noIoPrefix()
  val mem = Mem(Bits(32 bits), 1024)
  val read = io.bus.ar.fire
  val write = io.bus.w.fire
  val readaddress = Reg(UInt(10 bits)) init 0
  val writeaddress = Reg(UInt(10 bits)) init 0
  val rdata = Reg(Bits(32 bits)) init 0
  val wdata = Reg(Bits(32 bits)) init 0

  io.bus.aw.ready := io.bus.aw.valid
  io.bus.w.ready := io.bus.w.valid
  io.bus.b.valid := RegNext(io.bus.w.fire)
  io.bus.b.payload.resp := 0
  io.bus.b.payload.id := 0
  io.bus.r.payload.last := io.bus.r.fire
  io.bus.r.payload.data := mem.readSync(io.bus.ar.payload.addr,enable = read)
  io.bus.r.valid := RegNext(io.bus.ar.fire)
  io.bus.r.id := 0
  io.bus.r.payload.resp := 0
  io.bus.ar.ready := io.bus.ar.valid

  when(io.bus.ar.valid){
    readaddress := io.bus.ar.addr
  }
  when(io.bus.aw.valid){
    writeaddress := io.bus.aw.addr
  }

  when(io.bus.w.valid){
    wdata := io.bus.w.data
  }

  rdata := mem.readSync(readaddress,enable = read)

  mem.write(writeaddress,enable = write,data = io.bus.w.data)
}

object AxiMemory extends App{
  val axiconfig = Axi4Config(
    addressWidth = 10,
    dataWidth = 32,
    useId = true,
    idWidth = 3,
    useRegion = false,
    useBurst = false,
    useLock = false,
    useQos = false
  )
  val rtl = RtlConfig()
  rtl.setconfig(new SimpleAxiMemory(axiconfig))
}
