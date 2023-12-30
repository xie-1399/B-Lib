package DefineMem.SRAM.DualPort

import spinal.core._

class MyRamDor2PH (
                    val Bits: Int,
                    val Word_Depth: Int,
                    val Add_Width: Int,
                    val AClockDomain: ClockDomain,
                    val BClockDomain: ClockDomain,
                    val path: String,
                    val withBitWrite: Boolean,
                    val useDualPort: Boolean,
                    val Multiplexier:Int
                  ) extends BlackBox {
  addGenerics(
    "Bits" -> MyRamDor2PH.this.Bits,
    "Word_Depth" -> MyRamDor2PH.this.Word_Depth,
    "Add_Width" -> MyRamDor2PH.this.Add_Width
  )

  val io = new Bundle {
    val QA = out Bits(Bits bits)
    val QB = ifGen(useDualPort) {out Bits(Bits bits)}
    val CLKA = in Bool()
    val CLKB = in Bool()
    val CENA = in Bool()
    val CENB = in Bool()
    val WENA = ifGen(useDualPort)   {in Bool()}
    val WENB = ifGen(useDualPort)   {in Bool()}
    val BWENA = ifGen(useDualPort && withBitWrite) { in Bits(Bits bits) }
    val BWENB = ifGen(withBitWrite) { in Bits(Bits bits)}
    val AA   = in UInt(Add_Width bits)
    val AB   = in UInt(Add_Width bits)
    val DB   = in Bits(Bits bits)
    val DA   = ifGen(useDualPort) { in Bits(Bits bits) }
  }

  mapClockDomain(AClockDomain, io.CLKA)
  mapClockDomain(BClockDomain, io.CLKB)
  noIoPrefix()
  addRTLPath(path)
}
