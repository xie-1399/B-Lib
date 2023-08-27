package lib.Bus

import spinal.lib.bus.amba4.axi._
import spinal.core.sim._
//init axi bus first if use axi bus


object AxiInit{
  //init axi bus
  def apply(highSpeedBus: Axi4): Unit ={
    val ar = highSpeedBus.ar
    val r = highSpeedBus.r
    val aw = highSpeedBus.aw
    val w = highSpeedBus.w
    val b = highSpeedBus.b
    ar.ready #= false
    aw.ready #= false
    w.ready #= false
    r.valid #= false
    r.data #= 0
    if (r.config.useId) r.id #= 0
    if (r.config.useResp) r.resp #= 0
    if (r.config.useLast) r.last #= false
    if (r.config.useRUser) r.user #= 0

    b.valid #= false
    if (b.config.useId) b.id #= 0
    if (b.config.useResp) b.resp #= 0
    if (b.config.useBUser) b.user #= 0
  }
}
