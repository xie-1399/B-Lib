package DefineBus.Apb3

import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb.{Apb3, Apb3Config}

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
  




}
