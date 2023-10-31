package DefineRiscv.Core



case class coreParameters(resetValue:BigInt = 0x10000000l,
                          withRVC:Boolean = false,
                          TCMDepth:Int = 4096 * 4,
                          ioRange:BigInt = 0x1
                         ){
  def Xlen = 32
  def instructionWidth = 32
}


