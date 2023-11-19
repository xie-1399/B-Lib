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
  val realOp1 = Bits(p.Xlen bits)
  val realOp2 = Bits(p.Xlen bits)
  val pc = UInt(p.Xlen bits)
}

class Decode(p:coreParameters) extends PrefixComponent{
  import OP2._
  /* a simple function to get the IMM value */
  def getIMM(inst:Bits,immType:Bits):Bits = {
    val immBits = Bits(p.Xlen bits)
    immBits := immType.mux(
      IMM_I.asBits -> inst(31 downto 20).resized,
      IMM_S.asBits -> (inst(31 downto 25) ## inst(11 downto 7)).resized,
      IMM_B.asBits -> (inst(31) ## inst(7) ## inst(30 downto 25) ## inst(11 downto 8)).resized,
      IMM_U.asBits -> (inst(31 downto 12)).resize(p.Xlen),
      IMM_J.asBits -> (inst(31) ## inst(19 downto 12) ## inst(20) ## inst(30 downto 21)).resize(p.Xlen),
      default -> B"0".resized
    )
    immBits
  }

  val io = new Bundle{
    val decodeIn = slave Stream(FetchOut(p))
    val decodeOut = master Stream(DecodeOut(p))
    val error = out Bool()
    val rs1Data = in Bits(p.Xlen bits)
    val rs2Data = in Bits(p.Xlen bits)
    val rs1 = out UInt(5 bits)
    val rs2 = out UInt(5 bits)
  }

  val customDecode = new CustomDecode(decodeConfig.parameters)
  customDecode.io.valid := io.decodeIn.valid
  customDecode.io.inst := io.decodeIn.instruction

  val ctrl = customDecode.io.decodeOut
  val user1 = ctrl.illegal && ctrl.useRs1
  val user2 = ctrl.illegal && ctrl.useRs2
  io.error := customDecode.io.error
  io.rs1 := ctrl.rs1
  io.rs2 := ctrl.rs2
  /* the custom decode will read the reg file to get the reg value */

  /* the really op1 and op2 */
  val realOp1,realOp2 = Bits(p.Xlen bits)
  realOp1.clearAll()
  realOp2.clearAll()
  when(ctrl.op1 === OP1.RS1 && user1){
    realOp1 := io.rs1Data
  }.elsewhen(ctrl.op1 === OP1.PC && user1){
    realOp1 := io.decodeIn.payload.pc.asBits
  }

  when(ctrl.op2 =/= OP2.RS2 && !user2){
    realOp2 := getIMM(io.decodeIn.instruction,ctrl.op2.asBits)
  }.elsewhen(ctrl.op2 === OP2.RS2 && user2){
    realOp2 := io.rs2Data
  }

  /* with the decode out signal */
  io.decodeOut.payload.ctrl := ctrl
  io.decodeOut.payload.pc := io.decodeIn.pc
  io.decodeOut.payload.realOp1 := realOp1
  io.decodeOut.payload.realOp2 := realOp2
  io.decodeOut.arbitrationFrom(io.decodeIn)
}