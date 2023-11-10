package DefineRiscv

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.lib._
import spinal.core._
import DefineUntils.Untils._
/*
 * the common decode unit in the risc-v 32 Arch
 * support config set and custom instruction
 * version 1 : just support the I set and Csr instruction
 * just the combination logic enough
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
  val MUL,MULH,MULHSU,MULHU,DIV,DIVU,REM,REMU = newElement() /* add for the Mul and Div operation */
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
    MUL -> (9),
    MULH -> 10,
    MULHSU -> 11,
    MULHU -> 12,
    SRA -> (8 + 5),
    COPY -> 15,
    DIV -> 16,
    DIVU -> 17,
    REM -> 18,
    REMU -> 19
  )

}

/* memory operation at here */
object MemoryOp extends SpinalEnum{
  val LOAD,LOAD_U,STORE,NOT = newElement()
  defaultEncoding = SpinalEnumEncoding("memory")(
    NOT -> 0,
    LOAD -> 1,
    LOAD_U -> 2,
    STORE -> 3
  )
}

object Mask extends SpinalEnum{
  val WORD,HALF,BYTE = newElement()
  defaultEncoding = SpinalEnumEncoding("mask")(
    WORD -> 15,
    HALF -> 3,
    BYTE -> 1
  )
}

object OP1 extends SpinalEnum(binarySequential){
  val RS1,PC = newElement()
}
object OP2 extends SpinalEnum(binarySequential){
  val IMM_I,IMM_S,IMM_B,IMM_J,IMM_U,RS2 = newElement()
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
  val op1 = OP1()
  val op2 = OP2()
  val mask = Mask()
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


class CustomDecode(p:decodeParameters) extends PrefixComponent{
  import Instructions._
  import DecodeConstant._
  import InstructionFMT._
  val io = new Bundle{
    val inst = in Bits(32 bits)
    val valid = in Bool()
    val decodeOut = out (CtrlSignals(p))
    val error = out Bool()
  }
  def Y = True
  def N = False
  val ctrl = CtrlSignals(p)
  /* switch of decode*/
  val opcode = io.inst(opcodeRange)
  val rs1 = io.inst(rs1Range).asUInt
  val rs2 = io.inst(rs2Range).asUInt
  val rd = io.inst(rdRange).asUInt
  assignBundleWithList(ctrl,Seq(N,N,N,N,N,N,N,U(0,5 bits),U(0,5 bits),U(0,5 bits),OP1.RS1, OP2.RS2,Mask.WORD,BR.N,ALU.COPY,MemoryOp.NOT))

  when(io.valid){
    when(opcode === IM_R_FMT || opcode === A_R_FMT){
      switch(io.inst) {
        is(ADD) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.ADD, MemoryOp.NOT))}
        is(SUB) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.SUB, MemoryOp.NOT))}
        is(XOR) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.XOR, MemoryOp.NOT))}
        is(OR) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.OR, MemoryOp.NOT))}
        is(AND) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.AND, MemoryOp.NOT))}
        is(SLL) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.SLL, MemoryOp.NOT))}
        is(SRL) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.SRL, MemoryOp.NOT))}
        is(SRA) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.SRA, MemoryOp.NOT))}
        is(SLT) {assignBundleWithList(ctrl, Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.SLT, MemoryOp.NOT))}
        is(SLTU) {assignBundleWithList(ctrl,Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.SLTU, MemoryOp.NOT))}
        /* Todo add more M and A instruction here */
        ifGen(p.withRVA){}
        ifGen(p.withRVM){ /* add the M extension */
          is(MUL){assignBundleWithList(ctrl,Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.MUL, MemoryOp.NOT))}
          is(MULH){assignBundleWithList(ctrl,Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.MULH, MemoryOp.NOT))}
          is(MULHU){assignBundleWithList(ctrl,Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.MULHU, MemoryOp.NOT))}
          is(MULHSU){assignBundleWithList(ctrl,Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.MULHSU, MemoryOp.NOT))}
          is(DIV){assignBundleWithList(ctrl,Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.DIV, MemoryOp.NOT))}
          is(DIVU){assignBundleWithList(ctrl,Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.DIVU, MemoryOp.NOT))}
          is(REM){assignBundleWithList(ctrl,Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.REM, MemoryOp.NOT))}
          is(REMU){assignBundleWithList(ctrl,Seq(Y, Y, Y, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.RS2, Mask.WORD,BR.N, ALU.REMU, MemoryOp.NOT))}
        }
      }
    }
      .elsewhen(equalWithList(opcode,Seq(I_IMM_FMT,I_LOAD_FMT,I_ENV_FMT,I_JALR_FMT))){
        /* Todo with ecall and ebreak */
        switch(io.inst){
          is(ADDI) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.WORD, BR.N, ALU.ADD, MemoryOp.NOT))}
          is(XORI) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.WORD, BR.N, ALU.XOR, MemoryOp.NOT))}
          is(ORI) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.WORD, BR.N, ALU.OR, MemoryOp.NOT))}
          is(ANDI) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.WORD, BR.N, ALU.AND, MemoryOp.NOT))}
          is(SLLI) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.WORD, BR.N, ALU.SLL, MemoryOp.NOT))}
          is(SRLI) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.WORD, BR.N, ALU.SRL, MemoryOp.NOT))}
          is(SRAI) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.WORD, BR.N, ALU.SRA, MemoryOp.NOT))}
          is(SLTI) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.WORD, BR.N, ALU.SLT, MemoryOp.NOT))}
          is(SLTIU) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.WORD, BR.N, ALU.SLTU, MemoryOp.NOT))}

          is(LB) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.BYTE, BR.N, ALU.ADD, MemoryOp.LOAD))}
          is(LH) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.HALF, BR.N, ALU.ADD, MemoryOp.LOAD))}
          is(LW) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.WORD, BR.N, ALU.ADD, MemoryOp.LOAD))}
          is(LBU) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.BYTE, BR.N, ALU.ADD, MemoryOp.LOAD_U))}
          is(LHU) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.HALF, BR.N, ALU.ADD, MemoryOp.LOAD_U))}

          is(JALR) {assignBundleWithList(ctrl, Seq(Y, Y, N, Y, Y, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_I, Mask.WORD, BR.JR, ALU.ADD, MemoryOp.NOT))}
        }

      }
      .elsewhen(opcode === I_S_FMT){
        switch(io.inst){
          is(SB){assignBundleWithList(ctrl, Seq(Y, Y, Y, N, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_S, Mask.BYTE,BR.N, ALU.ADD, MemoryOp.STORE))}
          is(SH){assignBundleWithList(ctrl, Seq(Y, Y, Y, N, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_S, Mask.HALF,BR.N, ALU.ADD, MemoryOp.STORE))}
          is(SW){assignBundleWithList(ctrl, Seq(Y, Y, Y, N, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_S, Mask.WORD,BR.N, ALU.ADD, MemoryOp.STORE))}
        }
      }
      .elsewhen(opcode === I_B_FMT){
        switch(io.inst){
          is(BEQ){assignBundleWithList(ctrl, Seq(Y, Y, Y, N, Y, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_B, Mask.WORD,BR.EQ, ALU.ADD, MemoryOp.NOT))}
          is(BNE){assignBundleWithList(ctrl, Seq(Y, Y, Y, N, Y, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_B, Mask.WORD,BR.NE, ALU.ADD, MemoryOp.NOT))}
          is(BLT){assignBundleWithList(ctrl, Seq(Y, Y, Y, N, Y, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_B, Mask.WORD,BR.LT, ALU.ADD, MemoryOp.NOT))}
          is(BGE){assignBundleWithList(ctrl, Seq(Y, Y, Y, N, Y, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_B, Mask.WORD,BR.GE, ALU.ADD, MemoryOp.NOT))}
          is(BLTU){assignBundleWithList(ctrl, Seq(Y, Y, Y, N, Y, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_B, Mask.WORD,BR.LTU, ALU.ADD, MemoryOp.NOT))}
          is(BGEU){assignBundleWithList(ctrl, Seq(Y, Y, Y, N, Y, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_B, Mask.WORD,BR.GEU, ALU.ADD, MemoryOp.NOT))}
        }
      }
      .elsewhen(opcode === I_J_FMT){
        switch(io.inst){
          is(JAL){assignBundleWithList(ctrl, Seq(Y, N, N, Y, Y, N, N, rs1, rs2, rd, OP1.PC, OP2.IMM_J, Mask.WORD,BR.N, ALU.ADD, MemoryOp.NOT))}
        }
      }
      .elsewhen(equalWithList(opcode,Seq(I_LUI_FMT,I_AUIPC_FMT))){
        switch(io.inst){
          is(LUI){assignBundleWithList(ctrl, Seq(Y, N, N, Y, N, N, N, rs1, rs2, rd, OP1.RS1, OP2.IMM_U, Mask.WORD,BR.N, ALU.COPY, MemoryOp.NOT))}
          is(AUIPC){assignBundleWithList(ctrl, Seq(Y, N, N, Y, N, N, N, rs1, rs2, rd, OP1.PC, OP2.IMM_U, Mask.WORD,BR.N, ALU.ADD, MemoryOp.NOT))}
        }
      }
  }
  val outCtrl = ctrl
  io.decodeOut:= outCtrl
  io.error := io.valid && !outCtrl.illegal
}

object CustomDecode extends App{
  val rtl = new RtlConfig().GenRTL(top = new CustomDecode(decodeParameters()))
}