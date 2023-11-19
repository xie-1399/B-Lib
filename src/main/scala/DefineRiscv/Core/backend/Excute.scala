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
import spinal.core._
import spinal.lib._
import DefineSim.SpinalSim._
import DefineSim._
import DefineRiscv.Core._
import DefineRiscv.Core.frontend.{DecodeOut, decodeConfig}
import DefineRiscv.CtrlSignals
import DefineRiscv._


case class ExcuteOut(p:coreParameters) extends Bundle{
  val res = Bits(p.Xlen bits)
  val ctrl = CtrlSignals(decodeConfig.parameters)
  val pc = UInt(p.Xlen bits)
}

class Excute(p:coreParameters) extends PrefixComponent{
  import ALU._
  val io = new Bundle{
    val excuteIn = slave Stream(DecodeOut(p))
    val excuteOut = master Stream(ExcuteOut(p))
  }

  def isMulDiv(ctrl:CtrlSignals): Bool = {
    val res =  ctrl.alu.mux(
      MUL -> True,
      MULH -> True,
      MULHSU -> True,
      MULHU -> True,
      DIV -> True,
      DIVU -> True,
      REMU -> True,
      REM -> True,
      default -> False
    )
    res
  }


  val ctrl = io.excuteIn.ctrl
  val isMul = io.excuteIn.ctrl.illegal && isMulDiv(ctrl)

  /* set the alu unit */
  val alu = new Area{
    val aluUnit = new ALUPlugin(p)
    aluUnit.io.alu := ctrl.alu
    aluUnit.io.op1 := io.excuteIn.realOp1
    aluUnit.io.op2 := io.excuteIn.realOp2
  }
  io.excuteOut.arbitrationFrom(io.excuteIn)
  io.excuteOut.payload.pc := io.excuteIn.payload.pc
  io.excuteOut.payload.res := alu.aluUnit.io.res
  io.excuteOut.payload.ctrl := io.excuteIn.ctrl

  /* set the mul and div unit */
  val muldiv = new Area{
    //Todo add it support the mul and div
    
  }

}


object Excute extends App{
  val rtl = new RtlConfig().GenRTL(new Excute(coreParameters()))
}