package DefinePipeline.lib

/*
  using the spinal lib to create a simple 5 stages pipeline which can deal the Riscv-arith inst
  fetch -> decode -> excute -> memory -> writeback
  this is only show a way to use the pipeline
  */
import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import spinal.lib.pipeline._

import scala.collection.mutable.ArrayBuffer

object Constant{
  val instructionWidth = 32
  val Xlen = 32
}

case class signal() extends Bundle {} //or can set pipeline regs signal here

class PipelineCPU extends PrefixComponent{
  import Constant._
  val io = new Bundle{
    val inst = slave Stream(Bits(instructionWidth bits))
    val halt = in Bool()
    val flush = in Bool()
  }

  val pipeline = new Pipeline {
    val fetch = new Stage().setName("Fetch")
    val decode = new Stage().setName("Decode")
    val excute = new Stage().setName("Excute")
    val memory = new Stage().setName("Memory")
    val writeback = new Stage().setName("WriteBack")

    val PC = Stageable(UInt(Xlen bits))
    val Inst = Stageable(Bits(instructionWidth bits))
    val Halt = Stageable(Bool())
    val Flush = Stageable(Bool())
    val rs1 = Stageable(UInt(5 bits))
    val rs2 = Stageable(UInt(5 bits))
    val rd = Stageable(UInt(5 bits))
    val op1 = Stageable(Bits(Xlen bits))
    val op2 = Stageable(Bits(Xlen bits))
    val alu_res = Stageable(UInt(Xlen bits))
    val imm = Stageable(UInt(12 bits)) //may be set signal
    val useimm = Stageable(Bool())

    import Connection._

    connect(fetch, decode)(M2S())
    connect(decode, excute)(M2S())
    connect(excute, memory)(M2S())
    connect(memory, writeback)(M2S())

    // fetch stage just control the pc value
    val onFetch = new Area {
      import fetch._
      Halt := io.halt
      Flush := io.flush
      Inst := io.inst.payload
      val inc = RegInit(False)
      val pc = Reg(UInt(Xlen bits)).init(0x80000000l)
      val pcNext = Mux(inc, pc + 4 , pc)
      PC := pcNext

      when(!Halt && !Flush){
        inc := True
      }
      when(Halt){
        inc := False
        fetch.haltIt()
      }
      when(Flush){
        inc := False
        fetch.flushIt()
      }
      io.inst.ready := fetch.isReady
      valid := io.inst.valid
    }

    //fetch stage get the
    val onDecode = new Area {
      import decode._
      //only for add and addi

      rs1 := decode(Inst)(19 downto 15).asUInt
      rs2 := decode(Inst)(24 downto 20).asUInt
      rd := decode(Inst)(11 downto 7).asUInt
      imm := decode(Inst)(31 downto 20).asUInt
      val func3 = decode(Inst)(14 downto 12)
      val func7 = decode(Inst)(31 downto 25)
      val opcode = decode(Inst)(6 downto 0)
      useimm := False
      when(opcode === B"0010011"){
        useimm := True
      }

      assert(func3.asUInt === 0)
      assert(opcode === B"0110011" || opcode === B"0010011")

      val array:ArrayBuffer[BigInt] = new ArrayBuffer[BigInt]()
      for(idx <- 0 until 32) array += 0.toBigInt
      val regfile  = Mem(Bits(32 bits),32).initBigInt(array)

      op1 := regfile.readAsync(rs1,writeFirst)
      op2 := regfile.readAsync(rs2,writeFirst)
    }

    val onExcute = new Area {
      when(excute(useimm)){
        excute(alu_res) := (excute(imm) + excute(op1).asUInt).resized
      }.otherwise{
        excute(alu_res) := (excute(op1).asUInt + excute(op2).asUInt).resized
      }
    }

    val onMemory = new Area {
      //no memory operation
    }

    val onwriteBack = new Area {
      import writeback._
      onDecode.regfile.write(writeback(rd),writeback(alu_res).asBits,internals.input.valid)
    }
  }
  pipeline.build()
}