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
import DefineRiscv.ALU
import spinal.core._
import spinal.lib._
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import DefineRiscv.Core._
import DefineSim.SIMCFG

/* should know more about the M extension meaning
* and the simple mul is likely the alu plugin (but not good to use It) */

class SimpleMulDivPlugin(p:coreParameters) extends PrefixComponent {

  import ALU._

  val io = new Bundle {
    val valid = in Bool()
    val alu = in(ALU())
    val op1 = in Bits (p.Xlen bits)
    val op2 = in Bits (p.Xlen bits)
    val res = out Bits (p.Xlen bits)
  }
  val low = 31 downto 0
  val high = 63 downto 32

  /* the mul switch */
  val opSigned = io.alu.mux(
    MUL -> (B"11"),
    MULH -> (B"11"),
    MULHSU -> (B"10"),
    MULHU -> (B"00"),
    default -> (B"00")
  )

  val mulop1 = ((opSigned.lsb ? io.op1.msb | False) ## io.op1).asSInt
  val mulop2 = ((opSigned.msb ? io.op2.msb | False) ## io.op2).asSInt
  val temp = (mulop1 * mulop2)
  val lowRes = temp(low).asBits
  val highRes = temp(high).asBits

  val result = io.alu.mux(
    MUL -> lowRes,
    (MULH,MULHSU,MULHU) -> highRes,
    DIVU -> (io.op1.asUInt / io.op2.asUInt).asBits,
    DIV -> (io.op1.asSInt / io.op2.asSInt).asBits,
    REMU -> (io.op1.asUInt % io.op2.asUInt).asBits,
    REM -> (io.op1.asSInt % io.op2.asSInt).asBits,
    default -> io.op1
  )

  when(io.valid) {
    io.res := result
  }.otherwise {
    io.res := B(0, p.Xlen bits)
  }

}