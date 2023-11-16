package DefineMem.TCM

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.lib._
import spinal.core._
import spinal.core.sim._


/**
 * @param TCMSize support 32 KiB
 * @param physicalWidth the cmd request
 * @param dataWidth the dataWidth in the memory tcm
 * @param whiteBox debug it
 * @param useFlush generate flush signal
 * @param readOnly only for read
 * @param maskWidth write with mask
 */
case class TCMParameters(
                        TCMSize:BigInt = 32 KiB,
                        physicalWidth:Int = 32,
                        dataWidth:Int = 64,
                        whiteBox:Boolean = true,
                        useFlush:Boolean = false,
                        readOnly:Boolean = false,
                        maskWidth:Int = -1
                        ){

  def depth = TCMSize / dataWidth
  assert(isPow2(depth))
  if(!readOnly){assert(maskWidth != -1)}
  def addressRange = log2Up(depth) - 1 downto 0
}

class TCM(p:TCMParameters) extends PrefixComponent{
  import p._
  val io = new Bundle{
    val request = slave (TCMRequest(p))
    val flush = ifGen(useFlush){in Bool()}
  }

  val mem = Mem(Bits(dataWidth bits),depth)
  val ready = if(useFlush) !io.flush else True
  /* catch error when reading or writing happens in the flush process */
  val error = if(useFlush) io.flush && io.request.cmd.valid else False

  val wr = if(!readOnly) io.request.cmd.wr else False

  io.request.cmd.ready := ready
  io.request.rsp.payload.data := mem.readSync(io.request.cmd.address(addressRange),enable = io.request.cmd.io && io.request.cmd.fire && !wr)
  io.request.rsp.valid := RegNext(io.request.cmd.fire && !wr)
  io.request.rsp.error := error

  val write = ifGen(!readOnly){
    mem.write(io.request.cmd.address(addressRange),io.request.cmd.data,
      enable = io.request.cmd.fire && wr && io.request.cmd.io,mask = io.request.cmd.mask)
  }

  val flushIt = ifGen(useFlush){
    val flushCounter = Counter(depth)
    val flushDone = False
    mem.write(flushCounter.value,B(0,dataWidth bits),enable = io.flush)
    when(io.flush){
      flushCounter.increment()
    }
    flushDone.setWhen(flushCounter.willOverflowIfInc)
  }

  val WhiteBox = ifGen(whiteBox) {
      new Composite(this,"tcmWhiteBox"){
        mem.simPublic()
    }
  }
}
