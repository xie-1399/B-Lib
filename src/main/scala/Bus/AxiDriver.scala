package lib.Bus

//this part is about how to use axi4 bus read/write data from the memory
//they are more likely fixed
import spinal.core._
import spinal.lib.bus.amba4.axi._
import spinal.core.sim._

case class AxiDriver(axi:Axi4,clockDomain: ClockDomain,verbose:Boolean = false) {
  //init some axi4 signals
  axi.aw.valid #= false
  axi.w.valid #= false
  axi.b.ready #= false
  axi.ar.valid #= false
  axi.r.ready #= false

  def write(address: BigInt, data: BigInt): Unit = {
    if (verbose) println(s"AXI[0x${address.toString(16)}] = 0x${data.toString(16)}")
    axi.aw.valid #= true
    axi.aw.payload.addr #= address
    if(axi.config.useId)axi.aw.payload.id #= 0
    if(axi.config.useSize)axi.aw.payload.size #= 2
    if(axi.config.useLen)axi.aw.payload.len #= 0
    if(axi.config.useCache)axi.aw.payload.cache #= 0
    if(axi.config.useProt)axi.aw.payload.prot #= 0
    if(axi.config.useBurst)axi.aw.payload.burst #= 0 //fixed
    clockDomain.waitSamplingWhere(axi.aw.ready.toBoolean)

    axi.aw.valid #= false
    axi.w.valid #= true
    axi.w.payload.data #= data
    if(axi.config.useStrb)axi.w.strb #= 15
    if(axi.config.useLast)axi.w.last #= true
    clockDomain.waitSampling()
    axi.w.valid #= false

    clockDomain.waitSamplingWhere(axi.b.valid.toBoolean)
    axi.b.ready #= true
    axi.w.valid #= false
    clockDomain.waitSampling()
  }

  def read(address: BigInt): BigInt = {
    axi.ar.valid #= true
    axi.ar.payload.addr #= address
    if(axi.config.useId)axi.ar.payload.id #= 0
    if(axi.config.useLen)axi.ar.payload.len #= 0
    if(axi.config.useSize)axi.ar.payload.size #= 2
    if(axi.config.useCache)axi.ar.payload.cache #= 0
    if(axi.config.useProt)axi.ar.payload.prot #= 0
    if(axi.config.useBurst)axi.ar.payload.burst #= 0
    clockDomain.waitSamplingWhere(axi.ar.ready.toBoolean)
    axi.ar.valid #= false
    axi.r.ready #= true
    clockDomain.waitSampling()
    axi.r.payload.data.toBigInt
  }
}
