package lib.Math.FloatPoint
import spinal.core._

/*
this is base Bundle of the float point design
IEEE -< V = (-1)sign × M × pow(2,E)
some examples: https://www.toolhelper.cn/Digit/FractionConvert
*/

case class FPConfig(expSize:Int,mantSize:Int){
  //base formal of float
  def fullSize = 1 + expSize + mantSize
  def bias = (1 << (expSize - 1)) - 1
}

case class FPBase(fpConfig: FPConfig) extends Bundle{
  val signal = Bool()  //signal
  val exp = UInt(fpConfig.expSize bits)
  val mant = UInt(fpConfig.mantSize bits)

  //init with some value
  def init() = {
    signal init(False)
    exp init(0)
    mant init(0)
    this
  }

  //whether is normal number or special number
  def is_zero():Bool = exp === 0 && mant === 0 //just de formal
  def is_Nan():Bool = exp.andR && mant.orR //not a number(f != 0 )
  def is_inf():Bool = exp.andR && !mant.orR

  def set_Zero() = {
    signal := False
    exp := 0
    mant := 0
  }

  //get the real leading 1 (11 -> 111)
  def fullmant(): UInt = {
    (mant.resized(fpConfig.mantSize + 1).asBits | (U(1) << fpConfig.mantSize).asBits).asUInt
  }

  //get abs value
  def absolute():FPBase = {
    val abs = FPBase(fpConfig)
    abs.signal := False
    abs.exp := exp
    abs.mant := mant
    abs
  }
  //convert to Vec
  def toVec():Bits = {
    signal ## exp.asBits ## mant.asBits
  }
  // Vec convert to FP
  def fromVec(vec:Bits) = {
    signal := vec(fpConfig.expSize + fpConfig.mantSize)
    exp := vec(fpConfig.mantSize,fpConfig.expSize bits).asUInt  //offset , width
    mant := vec(0,fpConfig.mantSize bits).asUInt
  }
  //Todo convert double to FP
}

object FPBase{
  //use object get a FP
  def apply(expSize:Int,mantSize:Int):FPBase = {
    FPBase(FPConfig(expSize = expSize,mantSize = mantSize))
  }

  def apply(fpConfig: FPConfig): FPBase = {
    FPBase(fpConfig)
  }
}

object PipeInit{
  //the pipe data set initial value
  def apply[T <: Data](that: T, init: T, enable: Bool, pipeline: Boolean): T = if (pipeline) RegNextWhen(that, enable) init (init) else that
  def apply[T <: Data](that: T, init: T, pipeline: Boolean): T = apply(that, init, True, pipeline)
}

object JoinPipe{
  //create the pipeline value(if no enable -> True)
  def apply[T <: Data](that : T,enable:Bool,pipeline:Boolean): T = {
    if(pipeline) RegNextWhen(that,enable) else that
  }
  def apply[ T<:Data](that:T,pipeline:Boolean):T = {
    apply(that,True,pipeline)
  }
}

//Todo calculate the leading zero ?

