package lib.Math.FloatPoint.Arithmetic

import lib.Math.FloatPoint._
import spinal.core._

//the add step : https://juejin.cn/post/7085316206239940639

case class FpAddConfig(pipeStages: Int = 1){} //set initial pipeline stages

class FloatAdd(fpConfig:FPConfig,addConfig: FpAddConfig = null) extends Component{
  def pipeStages = if (addConfig == null) 1 else addConfig.pipeStages
  val io = new Bundle{
    val opValid = in Bool()
    val opRs1 = in (FPBase(fpConfig)) //set in with ()
    val opRs2 = in (FPBase(fpConfig))
    //wait for the results
    val resultValid = out Bool()
    val resultValue = out (FPBase(fpConfig))
  }

  //stage0 (check some special values)
  val stage0_Valid = io.opValid
  val stage0_Rs1 = io.opRs1
  val stage0_Rs2 = io.opRs2

  val stage0_Rs1_isZero = io.opRs1.is_zero()
  val stage0_Rs2_isZero = io.opRs2.is_zero()
  val stage0_isZero = stage0_Rs1_isZero || stage0_Rs2_isZero

  val stage0_Rs1_isInf = io.opRs1.is_inf()
  val stage0_Rs2_isInf = io.opRs2.is_inf()
  val stage0_isInf = stage0_Rs1_isInf || stage0_Rs2_isInf

  val stage0_Rs1_isNan = io.opRs1.is_Nan()
  val stage0_Rs2_isNan = io.opRs2.is_Nan()
  val stage0_isNan = stage0_Rs1_isNan || stage0_Rs2_isNan || (stage0_Rs1_isInf && stage0_Rs2_isInf && stage0_Rs1.signal =/= stage0_Rs2.signal)

  val stage0_Rs1_mant = io.opRs1.fullmant()
  val stage0_Rs2_mant = io.opRs2.fullmant()

  when(stage0_Rs1_isZero){
    stage0_Rs1_mant.clearAll()
  }
  when(stage0_Rs2_isZero){
    stage0_Rs2_mant.clearAll()
  }

  val exp_diff_1 = SInt(fpConfig.expSize + 1 bits)
  val exp_diff_2 = UInt(fpConfig.expSize bits)

  exp_diff_1 := stage0_Rs1.exp.resize(fpConfig.expSize + 1).asSInt - stage0_Rs2.exp.resize(fpConfig.expSize + 1).asSInt
  exp_diff_2 := stage0_Rs1.exp - stage0_Rs2.exp

  val sign_rs1 = Bool()
  val sign_rs2 = Bool()

  val exp_diff_overflow = Bool()
  val exp_diff = UInt(log2Up(fpConfig.mantSize) bits)
  val exp_add = UInt(fpConfig.expSize bits)

  val mant_rs1 = UInt(fpConfig.mantSize + 1 bits)
  val mant_rs2 = UInt(fpConfig.mantSize + 1 bits)

  //just rs1 >= rs2
  when(exp_diff_1 >=0){
    sign_rs1 := stage0_Rs1.signal
    sign_rs2 := stage0_Rs2.signal
    exp_add := stage0_Rs1.exp
    exp_diff_overflow := exp_diff_1 > fpConfig.mantSize
    exp_diff := exp_diff_1.resize(log2Up(fpConfig.mantSize)).asUInt
    mant_rs1 := stage0_Rs1_mant
    mant_rs2 := stage0_Rs2_mant
  }
    //just need to swap all
    .otherwise{
      sign_rs1 := stage0_Rs2.signal
      sign_rs2 := stage0_Rs1.signal
      exp_add := stage0_Rs2.exp
      exp_diff_overflow := exp_diff_2 > fpConfig.mantSize
      exp_diff := exp_diff_2.resize(log2Up(fpConfig.mantSize))
      mant_rs1 := stage0_Rs2_mant
      mant_rs2 := stage0_Rs1_mant
    }


  //stage1

}
