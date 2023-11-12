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
import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._


class Alu(p:coreParameters) extends PrefixComponent {
  import p._
  import ALU._
  val io  = new Bundle{
    val func = in(ALU)
    val doSub = in Bool()
    val src0 = in Bits(Xlen bits)
    val src1 = in Bits(Xlen bits)
    val result = out Bits(Xlen bits)
    val adder = out UInt(Xlen bits)
    val actual = out SInt(Xlen bits)
  }
  //add and sub(or just divide)
  val addSub = (io.src0.asSInt + Mux(io.doSub, ~io.src1, io.src1).asSInt + Mux(io.doSub,S(1),S(0))).asBits //notice not S"0"

  //if just want match some types(logic)
  val bitwise = io.func.mux(
    AND -> (io.src1 & io.src0),
    OR -> (io.src0 | io.src1),
    XOR -> (io.src0 ^ io.src1),
    default -> io.src0
  )

  //SLT SLTU
  val less = Mux(io.src0.asSInt < io.src1.asSInt , B"1", B"0") //Todo with msb


  //get results
  io.result := io.func.mux(
    (ADD,SUB) -> addSub,
    (SLT,SLTU) -> less.asBits.resized,
    default -> bitwise
  )
  io.adder := addSub.asUInt.resized
  io.actual := addSub.asSInt.resized
}

class Compare(srcWidth:Int) extends Component{
  //compare two numbers
  val io = new Bundle{
    val sign = in Bool()
    val src0 = in Bits(srcWidth bits)
    val src1 = in Bits(srcWidth bits)
    val ltx = out Bool()
    val eq = out Bool()
  }

  //sign or unsign compare
  when(io.sign) {
    io.ltx := io.src0.asSInt < io.src1.asSInt
  }.otherwise {
    io.ltx := io.src0.asUInt < io.src1.asUInt
  }
  io.eq := io.src0 === io.src1
}
