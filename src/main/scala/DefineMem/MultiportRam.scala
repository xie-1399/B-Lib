package DefineMem

import DefineSim.SpinalSim._
import spinal.core._
import spinal.lib._
import scala.collection.mutable.ArrayBuffer
import spinal.lib.eda.bench._
/*
 this file show about how to use multi ports read and write data form
 read the write can be parallel in the ram
 material: https://github.com/SpinalHDL/NaxRiscv/blob/main/src/main/scala/naxriscv/compatibility/MultiportRam.scala
*/

object MultiportRam {

  case class MemWriteCmd[T<:Data](payloadType:HardType[T],depth:Int) extends Bundle{
    val address = UInt(log2Up(depth) bits)
    val data = payloadType()
  }

  case class MemReadCmd[T<:Data](payloadType:HardType[T],depth:Int) extends Bundle with IMasterSlave{
    /* send the cmd and get the rsp*/
    val cmd = Flow(UInt(log2Up(depth) bits))
    val rsp = payloadType()
    override def asMaster(): Unit = {
      master(cmd)
      in(rsp)
    }
  }

  /*the most simple way for multi read -> make the ram multi bank（write all in the same line and read by it self）*/
  class RamMr1w[T<:Data](payloadType:HardType[T],readPort:Int,depth:Int,ReadAsync:Boolean = true) extends PrefixComponent{
    val io = new Bundle{
      val reads = Vec.fill(readPort)(slave(MemReadCmd(payloadType,depth)))
      val write = slave(Flow(MemWriteCmd(payloadType,depth))) /*also should control the write valid*/
    }
    val banks = for(read <- io.reads) yield new Area {
      val ram = Mem(payloadType,depth) // or use Mem.fill(depth)(payloadType)
      val memOperation = MemOperation.apply(payloadType,depth,0).addTag(unusedTag)
      ram.write(io.write.address,io.write.data,enable = io.write.valid)
      val readValue = if(ReadAsync) ram.readAsync(read.cmd.payload) else ram.readSync(read.cmd.payload)
      read.rsp := readValue
    }
  }



}
