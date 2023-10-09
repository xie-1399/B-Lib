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
    /* send the cmd and get the rsp */
    val cmd = Flow(UInt(log2Up(depth) bits))
    val rsp = payloadType()
    override def asMaster(): Unit = {
      master(cmd)
      in(rsp)
    }
  }

  case class RamMrMwIo[T<:Data](payloadType:HardType[T],depth:Int,readPort:Int,writePort:Int) extends Bundle{
    val reads = Vec.fill(readPort)(slave(MemReadCmd(payloadType,depth)))
    val writes = Vec.fill(writePort)(slave(Flow(MemWriteCmd(payloadType,depth))))
  }

  /*the most simple way for multi read -> make the ram multi bank（write all in the same line and read by it self）*/
  class RamMr1w[T<:Data](payloadType:HardType[T],readPort:Int,depth:Int,ReadAsync:Boolean = true) extends PrefixComponent{
    val io = new Bundle{
      val reads = Vec.fill(readPort)(slave(MemReadCmd(payloadType,depth)))
      val write = slave(Flow(MemWriteCmd(payloadType,depth))) /*also should control the write valid */
    }
    val banks = for(read <- io.reads) yield new Area {
      val ram = Mem(payloadType,depth) // or use Mem.fill(depth)(payloadType)
      val memOperation = MemOperation.apply(payloadType,depth,0).addTag(unusedTag)
      ram.write(io.write.address,io.write.data,enable = io.write.valid)
      val readValue = if(ReadAsync) ram.readAsync(read.cmd.payload) else ram.readSync(read.cmd.payload)
      read.rsp := readValue
    }
  }

  /*  if use xor -> a more safe way using the xor to change the data when write and read it with decode */
  class RamMrMw[T<:Data](payloadType:HardType[T],readPort:Int,writePort:Int,
                         depth:Int,ReadAsync:Boolean = true,UseXor:Boolean = false) extends PrefixComponent{
    if(UseXor) SpinalWarning("the multi port ram will encode the data with xor and decode it with xor too, so notice about it")
    val io = RamMrMwIo(payloadType,depth,readPort,writePort)
    val rawBits = payloadType.getBitsWidth
    val rawType = HardType(Bits(rawBits bits))

    /* set the data as bits type and use the hard type instead of Data T.clone() */
    val ram = List.fill(writePort)(MemOperation(rawType,depth))

    val writes = for((write,storage) <- (io.writes,ram).zipped) yield new Area{
      if(!UseXor){
        storage.write(write.address,write.data.asBits,write.valid)
      }
      else {
        val values = ram.filter(_ != storage).map(_.readAsync(write.address))
        val xored = (write.data.asBits :: values).reduceBalancedTree(_ ^ _) /* use the xor to encode the data */
        storage.write(
          enable = write.valid,
          address = write.address,
          data = xored
        )
      }
    }
    /* may be convert to the bits is a little over */
    val reads = for((read,storage) <- (io.reads,ram).zipped) yield new Area{
      if(!UseXor){
        val value = if (ReadAsync) storage.readAsync(read.cmd.payload) else storage.readSync(read.cmd.payload,enable = read.cmd.valid)
        read.rsp := value.as(payloadType)
      }
      else{
        val values = ram.map(_.readAsync(read.cmd.payload))
        val xored = values.reduceBalancedTree(_ ^ _)
        read.rsp := xored.as(payloadType)
      }

    }
  }

}
