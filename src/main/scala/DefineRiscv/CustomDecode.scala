package DefineRiscv

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.lib._
import spinal.core._
import DefineUntils.Others.{assignBundleWithList, _}
/*
 * the common decode unit in the risc-v 32 Arch
 * support config set and custom instruction
 * version 1 : just support the I set and Csr instruction
*/

case class decodeParameters(withRVI:Boolean = true,
                            withRVC:Boolean = false,
                            withRVM:Boolean = false,
                            withRVA:Boolean = false,
                            withRVF:Boolean = false,
                            withCsr:Boolean = true,
                            throwIllegal:Boolean = true)

/* the branch type has it's own encoding format*/
object BR extends SpinalEnum{
  val N, NE, EQ, GE, GEU, LT, LTU, J, JR = newElement()
  defaultEncoding = SpinalEnumEncoding("branch")(
    EQ -> 0,
    NE -> 1,
    J -> 2,
    JR -> 3,
    LT -> 4, //less(<)
    GE -> 5, //grater(>=)
    LTU -> 6,
    GEU -> 7,
    N -> 8 // Not a branch
  )
}

/* the alu type instruction here */
object ALU extends SpinalEnum{
  val ADD, SLL, SLT, SLTU, XOR, SRL, OR, AND, SUB, SRA, COPY = newElement()
  defaultEncoding = SpinalEnumEncoding("alu")(
    ADD -> 0,
    SLL -> 1,
    SLT -> 2,
    SLTU -> 3,
    XOR -> 4,
    SRL -> 5,
    OR -> 6,
    AND -> 7,
    SUB -> (8 + 0),
    SRA -> (8 + 5),
    COPY -> 15
  )
}

/* memory operation at here */
object MemoryOp extends SpinalEnum{
  val LOAD, STORE,NOT = newElement()
  defaultEncoding = SpinalEnumEncoding("memory")(
    NOT -> 0,
    LOAD -> 1,
    STORE -> 2
  )
}

object OP1 extends SpinalEnum(binarySequential){
  val RS1,PC,IMU = newElement()
}
object OP2 extends SpinalEnum(binarySequential){
  val IMM,RS2 = newElement()
}

case class CtrlSignals(p:decodeParameters) extends Bundle {
  /* Todo  set more control signal here like csr and more*/
  import p._
  val illegal = Bool()
  val useRs1 = Bool()
  val useRs2 = Bool()
  val useRd = Bool()
  val jump = Bool()
  val fencei = Bool()
  val compress = Bool()
  val rs1 = UInt(5 bits)
  val rs2 = UInt(5 bits)
  val rd = UInt(5 bits)
  val branch = BR()
  val alu = ALU()
  val memoryOption = MemoryOp()
  /* default : Seq(N,N,N,N,N,N,N,U(0,5 bits),U(0,5 bits),U(0,5 bits),BR.N,ALU.COPY,MemoryOp.NOT) */
}

object DecodeConstant{
  val rs1Range = 19 downto 15
  val rs2Range = 24 downto 20
  val rdRange = 11 downto 7
  val opcodeRange = 6 downto 0
}


class CustomDecode(p:decodeParameters,insert:Boolean = false) extends PrefixComponent{
  import Instructions._
  import DecodeConstant._
  import InstructionFMT._
  val io = new Bundle{
    val inst = slave Stream Bits(32 bits)
    val decodeOut = master Stream CtrlSignals(p)
    val error = out Bool()
  }
  def Y = True
  def N = False
  val ctrl = CtrlSignals(p)
  /* s complex switch of decode*/
  assignBundleWithList(ctrl,Seq(N,N,N,N,N,N,N,U(0,5 bits),U(0,5 bits),U(0,5 bits),BR.N,ALU.COPY,MemoryOp.NOT))
  val opcode = io.inst.payload(opcodeRange)
  val rs1 = io.inst.payload(rs1Range).asUInt
  val rs2 = io.inst.payload(rs2Range).asUInt
  val rd = io.inst.payload(rdRange).asUInt

  when(io.inst.valid){
    when(opcode === IM_R_FMT || opcode === A_R_FMT){
      switch(io.inst.payload) {
        is(ADD) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, BR.N, ALU.ADD, MemoryOp.NOT))}
        is(SUB) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, BR.N, ALU.SUB, MemoryOp.NOT))}
        is(XOR) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, BR.N, ALU.XOR, MemoryOp.NOT))}
        is(OR) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, BR.N, ALU.OR, MemoryOp.NOT))}
        is(AND) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, BR.N, ALU.AND, MemoryOp.NOT))}
        is(SLL) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, BR.N, ALU.SLL, MemoryOp.NOT))}
        is(SRL) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, BR.N, ALU.SRL, MemoryOp.NOT))}
        is(SRA) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, BR.N, ALU.SRA, MemoryOp.NOT))}
        is(SLT) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, BR.N, ALU.SLT, MemoryOp.NOT))}
        is(SLTU) {assignBundleWithList(ctrl,Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, BR.N, ALU.SLTU, MemoryOp.NOT))}
        /* Todo add more M and A instruction here */
        ifGen(p.withRVA){}
        ifGen(p.withRVM){}
      }
    }
      .elsewhen(equalWithList(opcode,Seq(I_IMM_FMT,I_LOAD_FMT,I_ENV_FMT,I_JALR_FMT))){
      }
      .elsewhen(opcode === I_S_FMT){

      }
      .elsewhen(opcode === I_B_FMT){

      }
      .elsewhen(opcode === I_J_FMT){

      }
      .elsewhen(equalWithList(opcode,Seq(I_LUI_FMT,I_AUIPC_FMT))){
      }

  }

  val outCtrl = if(insert) RegNext(ctrl) else ctrl
  io.decodeOut.payload := outCtrl
  io.decodeOut.valid := outCtrl.illegal /* will catch the error */
  io.error := outCtrl.illegal
  io.inst.ready := outCtrl.illegal

}

object CustomDecode extends App{
  val rtl = new RtlConfig().GenRTL(top = new CustomDecode(decodeParameters()))
}