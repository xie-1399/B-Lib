package DefineRiscv.Core



case class coreParameters(resetValue:BigInt = 0x10000000l,
                          withRVC:Boolean = false,
                          ioRange:BigInt = 0x1,
                          whiteBox:Boolean = true
                         ){
  def Xlen = 32
  def instructionWidth = 32
}


