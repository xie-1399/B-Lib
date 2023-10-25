package DefineBus.Apb3

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb.sim.Apb3Driver
import spinal.lib.bus.amba3.apb.{Apb3, Apb3Config}
import spinal.core.sim._
/*
some useful methods to use the apb api and operate the apb bus
the clear signal meaning can see : https://zhuanlan.zhihu.com/p/419750074 and pro
PADDR、PSEL、PENABLE、PREADY、PWRITE、PWDATA、PRDATA、PSLVERROR
*/

object APBOperation {

  /*
  in this simple way you can get a APB bus
  */
  def create(config:Apb3Config):Apb3 = {
    val bus = Apb3(config)
    bus
  }

  /*
  connect from each other with latency or not
  */
  def connect(source:Apb3,dest:Apb3,masterlatency:Boolean) = {
    if(masterlatency) source.m2sPipe() >> dest else source >> dest
  }

  /*
  sim with the apb driver and catch will print the driver data
  */
  def sim(apb : Apb3, clockDomain : ClockDomain) = {
    val driver = Apb3Driver(apb,clockDomain)
    driver
  }

  /* catch the write data in the data range */
  def catchW(apb : Apb3, base:BigInt, offset:BigInt,log:Boolean = true) = {
      val Cond = apb.PENABLE.toBoolean && apb.PREADY.toBoolean &&
        apb.PWRITE.toBoolean && apb.PSEL.toBigInt == 1 && apb.PADDR.toBigInt >= base && base + offset >= apb.PADDR.toBigInt
      if (Cond) {
        if (log) print(apb.PWDATA.toBigInt.toChar) /* with char out */
      }
  }


}
