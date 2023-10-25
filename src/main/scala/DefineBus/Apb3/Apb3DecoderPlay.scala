package DefineBus.Apb3

import DefineSim.SIMCFG
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.lib.bus.amba3.apb._
import spinal.lib.com.uart._
import spinal.lib.com.uart.sim._
/* the apb decoder will choose with the apb decode to send the request */
import spinal.core.sim._
import scala.math.BigDecimal._
import spinal.core._
import spinal.lib._

/* on the uart ctrl can use some regs represent the status of the uart
 * show the bus can write it */

class Apb3DecoderPlay extends PrefixComponent{
  val io =  new Bundle{
    val apb = slave (APBOperation.create(Apb3Config(addressWidth = 32,dataWidth = 32)))
    val uart = master(Uart())
  }

  val init = UartCtrlInitConfig(
    baudrate = 921600,
    dataLength = 7, // 8 bits
    parity = UartParityType.NONE,
    stop = UartStopType.ONE
  )

  val uartConfig = UartCtrlMemoryMappedConfig(
    uartCtrlConfig = UartCtrlGenerics(
      dataWidthMax = 8 ,
      clockDividerWidth = 20,
      preSamplingSize = 1,
      samplingSize = 5,
      postSamplingSize = 2
    ),
    initConfig = init,
    busCanWriteClockDividerConfig = true,
    busCanWriteFrameConfig = false,
    txFifoDepth = 16,
    rxFifoDepth = 16
  )

  val apb3UartCtrl = Apb3UartCtrl(uartConfig)
  apb3UartCtrl.io.apb.addAttribute(Verilator.public)
  apb3UartCtrl.io.uart <> io.uart
  val decoder = Apb3Decoder(
    master = io.apb,
    /* the value should set with the address width */
    slaves = List(
      apb3UartCtrl.io.apb -> (0x10000000,16 KiB)
    )
  )
}

/* using the catch way is quick than waiting the uart */

object Apb3DecoderPlay extends App{
  import spinal.core.sim._
  SIMCFG(gtkFirst = true,compress = true).compile{
    /* set the frequency is 50 MHZ */
    val dut = new RtlConfig(frequency = 100).GenRTL(new Apb3DecoderPlay())
    dut
  }.doSimUntilVoid{
    dut =>

      /* clock divider:((ClockDomain.current.frequency.getValue / baudrate / reg.g.rxSamplePerBit).toInt-1) */
      /*  baudrate = Fclk / (rxSamplePerBit*clockDividerWidth)  */
      val code = List(0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x20, 0x77, 0x6F, 0x72, 0x6C, 0x64, 0x0A)
      val frequency = dut.clockDomain.frequency.getValue.toDouble
      println("frequecy:" + frequency)
      val uartBaudPeriod = 921600
      val width = (frequency / uartBaudPeriod / 8).setScale(0, BigDecimal.RoundingMode.HALF_DOWN).toBigInt - 1
      println("width:" + width)

      dut.clockDomain.forkStimulus((1e12/frequency).toLong)
      val driver = APBOperation.sim(dut.io.apb,dut.clockDomain)

      /* so must set the config first and then wait sample until it works */
      def DecoderMonitor() = {
        driver.write(0x10000008,11) /*set some fragment information  */
        val thread = fork{
          while (true){
            for(idx <- 0 until 60){
              driver.write(0x10000000,code((idx) % 12))
              sleep(uartBaudPeriod * 100)
            }
            simSuccess()
          }
        }
        val uartTx = UartDecoder(
          uartPin = dut.io.uart.txd,
          baudPeriod = uartBaudPeriod
        )
      }
      /* use the decoder should slow refresh it*/
      // DecoderMonitor()

      def write(address: BigInt, data: BigInt): Unit = {
        dut.io.apb.PSEL #= 1
        dut.io.apb.PENABLE #= false
        dut.io.apb.PWRITE #= true
        dut.io.apb.PADDR #= address
        dut.io.apb.PWDATA #= data
        dut.clockDomain.waitSampling()
        dut.io.apb.PENABLE #= true
        dut.clockDomain.waitSamplingWhere(dut.io.apb.PREADY.toBoolean)
        APBOperation.catchW(dut.apb3UartCtrl.io.apb,0,0)
        dut.io.apb.PSEL #= 0
        dut.io.apb.PENABLE.randomize()
        dut.io.apb.PADDR.randomize()
        dut.io.apb.PWDATA.randomize()
        dut.io.apb.PWRITE.randomize()
      }

      /* get the  */
      def CatchBus() = {
        val apb = dut.apb3UartCtrl.io.apb
        for(idx <- 0 until 60){
          write(0x10000000,code((idx) % 12))
        }
        simSuccess()
      }
      CatchBus()
  }

}