package DefineDevice.uart

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._

/*
  the tx recevice the stream uart fram -> convert it to the txd (one bit by one bit)
  the source file : https://github.com/SpinalHDL/SpinalHDL/blob/dev/lib/src/main/scala/spinal/lib/com/uart/UartCtrlTx.scala
 */

class Tx(gen:UartGen) extends PrefixComponent{
  import gen._

  val io = new Bundle{
    val frame = in(UartCtrlFrameConfig(gen))
    val cts = in Bool()
    val write = slave Stream(Bits(dataWidthMax bits))
    val break = in Bool()
    val samplingTick = in Bool()
    val txd = out Bool()
  }

  /* used for the baud time data translate */
  val clockDivider = new Composite(this,"ClockDivider"){
    val counter = Counter(rxSamplePerBit)
    val tick = counter.willOverflow
    when(io.samplingTick){
      counter.increment()
    }
  }

  val tickCounter = new Composite(this,"TickCounter"){
    val value = Reg(UInt(Math.max(log2Up(dataWidthMax),2) bits)).init(0)
    def reset() = {value := 0}
    when(clockDivider.tick){
      value := value + 1
    }
  }

  /* use a statemachine to control the whole process */
  val stageMachine = new Composite(this,"TxStateMachine"){
    import UartTxState._
    import UartStopType._

    val state = RegInit(IDLE)
    val parity = Reg(Bool())
    val txd = True

    when(clockDivider.tick){
      parity := parity ^ txd
    }
    io.write.ready := io.break

    switch(state){
      is(IDLE){
        when(io.write.valid && !io.cts && clockDivider.tick){
          state := START
        }
      }
      is(START){
        txd := False
        when(clockDivider.tick){
          state := DATA
          parity := io.frame.parity === UartParityType.ODD
          tickCounter.reset()
        }
      }
      is(DATA){
        txd := io.write.payload(tickCounter.value)
        when(clockDivider.tick){
          when(tickCounter.value === io.frame.dataLength){
            io.write.ready := True
            tickCounter.reset()
            when(io.frame.parity === UartParityType.NONE){
              state := STOP
            }otherwise{
              state := PARITY
            }
          }
        }
      }

      is(PARITY){
        txd := parity
        when(clockDivider.tick){
          state := STOP
          tickCounter.reset()
        }
      }
      is(STOP){
        when(clockDivider.tick){
          when(tickCounter.value === toBitCount(io.frame.stop)){
            state := io.write.valid ? START | IDLE
          }
        }
      }
    }
  }
  io.txd := RegNext(stageMachine.txd && !io.break) init(True)
}
