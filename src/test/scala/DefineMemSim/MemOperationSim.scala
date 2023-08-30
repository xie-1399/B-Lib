package DefineMemSim
import DefineSim.SpinalSim._
import spinal.core._
import DefineSim._
import spinal.lib._
import scala.collection.mutable.ArrayBuffer
import DefineMem._

  /*
  add test of mem operations(ports and stream/flow to wr)
  */

class MemOperationSim(init:Boolean = false) extends PrefixComponent {
  val io = new Bundle{
    val cmd = slave Stream(UInt(5 bits))
    val streamreadsync = master Stream(Bits(32 bits))
  }
  val memory = Mem(Bits(32 bits),32)
  val arrayBuffer = ArrayBuffer[BigInt]()
  if(init) for (i <- 0 until 32) arrayBuffer += i.toBigInt;memory.initBigInt(arrayBuffer.toSeq)
  val operation = new MemOperation(memory)
  io.streamreadsync <-< operation.StreamReadSync(io.cmd)
}


