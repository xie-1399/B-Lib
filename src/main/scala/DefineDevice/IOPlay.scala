package DefineDevice

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._
import spinal.lib.io._

class ReadableOpenDrainPlay extends PrefixComponent{
  /* just wrapper the data with the write and read driver */
  val io = new Bundle{
    val dataBus = master(ReadableOpenDrain(Bits(32 bits)))
  }
  val writeable = RegInit(False)
  when(io.dataBus.read === 10){
    writeable := True
  }
  io.dataBus.write := 10
}

class TriStatePlay extends PrefixComponent{
  /* a little like the ReadableOpenDrain with enable */

  val io = new Bundle{
    val dataBus = master(TriState(Bits(32 bits)))
  }

  io.dataBus.write := 0x100
  io.dataBus.writeEnable := True /* with the write enable signal */

}

object IOPlay extends App{
  /* so simple that don't need add simulation more */
  val rtl = new RtlConfig().GenRTL(top = new ReadableOpenDrainPlay())
  val rtl2 = new RtlConfig().GenRTL(top = new TriStatePlay())
}