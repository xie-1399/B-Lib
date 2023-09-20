package DefineDevice.uart.source

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._

 /*
  the rx will receive the data with rxd into the fifo
  the source code is around https://github.com/SpinalHDL/SpinalHDL/blob/dev/lib/src/main/scala/spinal/lib/com/uart/UartCtrlRx.scala
 */

class Rx(gen:UartGen) extends PrefixComponent{
  import gen._

  val io = new Bundle{
    val frame = in (UartCtrlFrameConfig(gen))
    val samplingTick = in Bool()
    val read = master Stream(Bits(dataWidthMax bits))
    val rxd = in Bool()
    val rts = out Bool()  //show whether the rx buffer is full
    val error = out Bool()
    val break = out Bool()
  }

  io.error := False
  io.rts := RegNext(!io.read.ready)init(False)

  val sampler = new Composite(this,"Rx_Sampler"){
    val sync = BufferCC(io.rxd,init = False)
    val samples = History(that = sync, length = samplingSize,when = io.samplingTick,init = True) /* like the five regs sequence in the sample */
    val value = RegNext(MajorityVote(samples)).init(True)
    val tick = RegNext(io.samplingTick) init(False)
  }

  val bitTimer = new Area {
    val counter = Reg(UInt(log2Up(rxSamplePerBit) bits))
    def reset = counter := preSamplingSize + (samplingSize - 1) / 2 - 1
    val tick = False
    when(sampler.tick){
      tick := True
      if(!isPow2(rxSamplePerBit)){
        counter := rxSamplePerBit - 1
      }
    }
  }

  val bitCount = new Area {
    val value = Reg(UInt(Math.max(log2Up(dataWidthMax), 2) bit))
    def reset() = value := 0
    /* when the bitTimer tick is high -> shows that need to sample*/
    when(bitTimer.tick){
      value := value + 1
    }
  }

  val break = new Area {

  }

  val stateMachine = new Composite(this,"Rx_StateMachine"){
    import UartState._
    val state = RegInit(IDLE)
    val parity = RegInit(Bool())
    val shifter = Reg(io.read.payload)
    val validReg = RegNext(False).init(False)

    when(bitTimer.tick) {
      parity := parity ^ sampler.value
    }

    switch(state){
      is(IDLE){
        when(sampler.tick && !sampler.value &&)
      }
      is(){

      }
      is(){

      }
      is(){

      }
    }
  }
}
