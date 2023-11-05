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

package DefineRiscv.Core.frontend

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.lib._
import spinal.core._
import DefineRiscv.Core.coreParameters
import DefineRiscv._

/* just using the custom decode unit trans the instruction */

object decodeConfig{
  /* support the I M Set*/
  def parameters = decodeParameters(withRVI = true,withRVM = true,withCsr = true)
}

case class DecodeOut(p:coreParameters) extends Bundle{
  val ctrl = CtrlSignals(decodeConfig.parameters)
  val pc = UInt(p.Xlen bits)
}

class Decode(p:coreParameters) extends PrefixComponent{

  val io = new Bundle{
    val decodeIn = slave Stream(FetchOut(p))
    val decodeOut = master Stream(DecodeOut(p))
  }

  val customDecode = new CustomDecode(decodeConfig.parameters)
  val inst = Stream(Bits(p.instructionWidth bits))
  inst.arbitrationFrom(io.decodeIn)
  inst.payload := io.decodeIn.payload.instruction
  inst >> customDecode.io.inst

  io.decodeOut.arbitrationFrom(customDecode.io.decodeOut)
  io.decodeOut.payload.ctrl := customDecode.io.decodeOut
  io.decodeOut.payload.pc := io.decodeIn.pc
}

object Decode extends App{
  val rtl = new RtlConfig(path = "temp").GenRTL(top = new Decode(coreParameters()))
}