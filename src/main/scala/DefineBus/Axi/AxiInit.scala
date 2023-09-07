package DefineBus.Axi

import spinal.lib.bus.amba4.axi._
import spinal.core.sim._
//init axi bus first if use axi bus


object AxiInit{
  //init axi4 slave bus
  def apply(bus: Axi4): Unit ={
    val ar = bus.ar
    val r = bus.r
    val aw = bus.aw
    val w = bus.w
    val b = bus.b
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

  //AxiReadOnly init slave bus
  def apply(bus:Axi4ReadOnly,readOnly:Boolean) = {
    require(readOnly)
    val ar = bus.ar
    val r = bus.r
    ar.ready #= false
    r.valid #= false
    r.data #= 0
    if (r.config.useId) r.id #= 0
    if (r.config.useResp) r.resp #= 0
    if (r.config.useLast) r.last #= false
    if (r.config.useRUser) r.user #= 0
  }
}
