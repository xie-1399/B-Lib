/** *************************************************************************************
 * MIT License
 *
 * Copyright (c) 2023 xxl
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * ************************************************************************************* */

package DefineRiscv.Core.backend
import DefineRiscv._
import DefineRiscv.Core._
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._

/* rebuild the ALU unit and contains all */

class ALUPlugin(p:coreParameters) extends PrefixComponent{
  import ALU._
  val io = new Bundle{
    val alu = in (ALU())
    val valid = in Bool()
    val op1 = in Bits(p.Xlen bits)
    val op2 = in Bits(p.Xlen bits)
    val res = out Bits(p.Xlen bits)
  }

  val bitsCal = io.alu.mux(
    AND -> (io.op1 & io.op2),
    OR -> (io.op1 | io.op2),
    XOR -> (io.op1 ^ io.op2),
    SLL -> (io.op1 |<< io.op2.asUInt), /* logic shift */
    SRL -> (io.op1 |>> io.op2.asUInt),
    SRA -> (io.op1.asSInt >> io.op2.asUInt).asBits, /* the arithmetic shift using */
    default -> io.op1
  )
  val lessU = io.alu === SLTU
  val less = Mux(lessU,io.op1.asUInt < io.op2.asUInt,io.op1.asSInt < io.op2.asSInt)

  val doSub = io.alu === SUB
  val addSub = Mux(doSub,io.op1.asSInt - io.op2.asSInt,io.op1.asSInt + io.op2.asSInt).asBits

  when(io.valid){
    io.res := io.alu.mux(
        (SLT,SLTU) -> less.asBits.resized,
        (ADD,SUB) -> addSub,
        default -> bitsCal
      )
  }.otherwise{
    io.res := B(0,p.Xlen bits)
  }
}