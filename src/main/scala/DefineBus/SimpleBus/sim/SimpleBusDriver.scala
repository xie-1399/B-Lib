package DefineBus.SimpleBus.sim

import spinal.core._
import spinal.lib._
import spinal.sim._
import spinal.core._
import scala.math._
import spinal.core.sim._
import DefineBus.SimpleBus.SMB
  /*
  drive the simple bus bundle signal
  */

class SMBDriver(smb:SMB,clockDomain: ClockDomain){

  val config = smb.getConfig()
  def read(addr: BigInt,sync:Boolean = false): BigInt = {
    smb.valid #= false
    clockDomain.waitSampling()
    smb.valid #= true
    smb.address #= addr
    smb.write #= false
    smb.mask #= 0
    smb.wdata #= 0
    clockDomain.waitSamplingWhere(smb.valid.toBoolean && smb.ready.toBoolean)
    if(sync){
      clockDomain.waitSampling()
      smb.rdata.toBigInt
    }else{
      smb.rdata.toBigInt
    }
  }

  def write(addr:BigInt,data:BigInt,mask:Int) = {
    assert(mask < (pow(2,config.maskWidth)))
    smb.valid #= false
    clockDomain.waitSampling()
    smb.valid #= true
    smb.address #= addr
    smb.write #= true
    smb.mask #= mask
    smb.wdata #= data
    clockDomain.waitSamplingWhere(smb.valid.toBoolean && smb.ready.toBoolean)
  }
}
