package DefineMem
import spinal.core._
import spinal.lib._
import DefineUntils._
/*
  define some useful memory operation here(adn some comes from lib Mem)
  more details and methods is on : https://github.com/SpinalHDL/SpinalHDL/blob/dev/lib/src/main/scala/spinal/lib/Mem.scala
*/

class MemOperation[T <: Data](mem:Mem[T]) {
    /*
    the stream and flow way to operation(No Cross clock)
    control the ret.ready to block the cmd.fire
    */
  def StreamReadSync(cmd:Stream[UInt]):Stream[T] = {
    val ret = Stream(mem.wordType)
    val retValid = RegInit(False).clearWhen(ret.ready)
    val retData = mem.readSync(cmd.payload,enable = cmd.fire,clockCrossing = false)
    when(cmd.ready){
      retValid := cmd.valid
    }
    cmd.ready := ret.isFree  //!valid || ready
    ret.valid := retValid
    ret.payload := retData
    ret
  }

  def FlowReadSync(cmd:Stream[UInt]) : Flow[T] = {
    /*
    the flow control is a vey easy way to read sync the memory
    */
    val ret = Flow(mem.wordType)
    val retData = mem.readSync(cmd.payload,enable = cmd.fire,clockCrossing = false)
    ret.valid := RegNext(cmd.valid)
    ret.payload := retData
    ret
  }
  /*
  in fact just a condition read
  */
  def FlowReadAsync(cmd:Stream[UInt]) : Flow[T] = {
    val ret = Flow(mem.wordType)
    val retData = mem.readAsync(cmd.payload)
    ret.valid := cmd.valid
    ret.payload := retData
    ret
  }

  /*
  multi Stream/Flow readSync ports
  */
  def StreamReadSyncMultiPort(cmd:Seq[Stream[UInt]],crossClock:Boolean = false):Vec[Stream[T]] = {
    val ret = Vec(Stream(mem.wordType),cmd.length)
    val selectedOh = OneHotOperation.BoolswordLSBOh(Vec(cmd.map(_.valid)))
    val selectedCmd = Stream(mem.addressType)
    selectedCmd.valid := cmd.map(_.valid).orR  //get any valids cmd
    selectedCmd.payload := OneHotOperation.BitVectorOhMapping(selectedOh.asBits,Vec(cmd.map(_.payload)))

    val retOh = RegNextWhen(selectedOh,selectedCmd.ready)
    val retRsp = StreamReadSync(selectedCmd)

    retRsp.ready := (ret, retOh).zipped.map(_.ready && _).orR
    for (i <- 0 until cmd.length) {
      cmd(i).ready := selectedCmd.ready && selectedOh(i)
      ret(i).valid := retRsp.valid && retOh(i)
      ret(i).payload := retRsp.payload
    }
    ret
  }



  /*
  define memory read/write ports
  1 : support mask(or not) write ports 2: support read async/sync way
  */
  def writePort():Flow[WritePortCmd[T]] = {
    val ret = Flow(WritePortCmd(mem))
    when(ret.valid){
      mem.write(ret.address,ret.payload.data)
    }
    ret
  }
  def writeMaskPort(maskWidth:Int): Flow[WritePortCmdWithMask[T]] = {
    val ret = Flow(WritePortCmdWithMask(mem,maskWidth))
    mem.write(ret.address, ret.payload.data,ret.valid,ret.mask) //how about the mask work
    ret
  }

  /*
  get this port -> will read the ret address Async at once
  */
  def readAsyncPort():ReadPortAsyncRequest[T] = {
    val ret = ReadPortAsyncRequest(mem.wordType(),mem.addressWidth)
    ret.data := mem.readAsync(ret.address)
    ret
  }

  def readSyncPort():ReadPortSyncRequeset[T] = {
    val ret = ReadPortSyncRequeset(mem.wordType(),mem.addressWidth)
    ret.rsp := mem.readSync(ret.cmd.payload,enable = ret.cmd.valid)
    ret
  }

}

case class WritePortCmd[T <: Data](mem: Mem[T],maskWidth:Int = -1) extends Bundle{
  def useMask = maskWidth >= 0
  val address = mem.addressType()
  val data = mem.wordType()
  val mask = ifGen(useMask) (Bits(maskWidth bits))
}

case class WritePortCmdWithMask[T <: Data](mem: Mem[T],maskWidth:Int) extends Bundle{
  val address = mem.addressType()
  val data = mem.wordType()
  val mask = Bits(maskWidth bits)
}

case class ReadPortAsyncRequest[T <: Data](dataType: T,addressWidth:Int) extends Bundle with IMasterSlave{
  val address = UInt(addressWidth bits)
  val data = cloneOf(dataType)
  override def asMaster(): Unit = {
    out(address)
    in(data)
  }
}

case class ReadPortSyncRequeset[T <: Data](dataType:T,addressWidth:Int) extends Bundle with IMasterSlave{
  val cmd = Flow(UInt(addressWidth bits))
  val rsp = cloneOf(dataType)

  override def asMaster(): Unit = {
    master(cmd)
    in(rsp)
  }
  /*
  Bypass read if write happens(get the write data)
  */
  def writeFirst(writeLast:Flow[WritePortCmd[T]]) = {
   val cmdLast = RegNextWhen(cmd.payload,cmd.valid)
   val hit = cmdLast ===  writeLast.address && writeLast.valid
    when(hit){
      rsp := writeLast.data
    }
  }
}