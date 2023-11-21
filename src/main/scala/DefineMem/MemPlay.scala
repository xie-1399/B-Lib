package DefineMem

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._

/**
 * the spinal lib has lots of func to map the memory write or read it
 * here set it to example (Todo add more)
 */

case class MemParameters(
                        bankNum : Int = 1,
                        depth : Int,
                        dataWidth : Int
                        )
/* support write mask  */
class MemPlay(p:MemParameters) extends PrefixComponent{
  import p._

  val io = new Bundle{
    val valid = in Bool()
    val addr = in UInt(log2Up(depth) bits)
    val wr = in Bool()
    val data = in Bits(dataWidth bits)
  }

  val banks = List.fill(bankNum)(
    Mem(Bits(dataWidth bits),depth)
  )

  val counter = Counter(bankNum,io.wr)
  /* for each bank can set a read and write port */
  val read = banks.map(_.readSyncPort)
  read.map(_.cmd.valid := io.valid)
  read.map(_.cmd.payload := io.addr)

  val write = banks.map(_.writePort()) /* also can set the mask */
  write.map{
    bank =>
      /* no mask about it */
      bank.valid := io.valid && io.wr
      bank.payload.data := io.data
      bank.payload.address := io.addr
  }

  val read_value = read(counter.value).rsp
}

object MemPlay extends App{
  val rtl = new RtlConfig().GenRTL(new MemPlay(MemParameters(4,16,32)))
}