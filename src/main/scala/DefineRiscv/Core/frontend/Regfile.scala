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

import spinal.core._
import spinal.lib._
import DefineSim.SpinalSim.PrefixComponent
import DefineRiscv.Core._

/* notice : keep the value for the x0 register */

case class RegfileIO(p:coreParameters) extends Bundle with IMasterSlave {
  import p._
  val rs1 = UInt(5 bits)
  val rs2 = UInt(5 bits)
  val rs1Data = Bits(Xlen bits)
  val rs2Data = Bits(Xlen bits)
  val rd = UInt(5 bits)
  val data = Bits(Xlen bits)
  val write = Bool()

  override def asMaster(): Unit = {
    in(rs1,rs2,rd,data,write)
    out(rs1Data,rs2Data)
  }
}

class Regfile(p:coreParameters) extends PrefixComponent{
  val io = master (RegfileIO(p))

  /* set register file name here Or Just use the x0~x31*/
  private val registerNames = Seq("zero", "ra", "sp", "gp", "tp", "t0", "t1", "t2", "s0_fp", "s1", "a0", "a1", "a2", "a3", "a4",
    "a5", "a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6"
  )
  val regfile = Mem(Bits(p.Xlen bits),32)

  def read(addr:UInt,regfileReadKind: RegFileReadKind): Bits = {
    /* the way to match it */
    val data = regfileReadKind match {
      case `Async` => regfile.readAsync(addr,writeFirst)
      case `Sync` => regfile.readSync(addr)
    }
    val readData = addr.mux(
      U(0) -> B(0,p.Xlen bits),
      default -> data //use write first
    )
    readData
  }

  /* set regs name for debug and more */
  if (p.whiteBox) {
    for (i <- 0 until 32) {
      val regWire = Bits(p.Xlen bits)
      regWire.setName(s"_${i}_" + registerNames(i))
      regWire := regfile.readAsync(U(i).resized,writeFirst)  //why can't use read
    }
  }

  def write(addr:UInt,data:Bits): Unit = {regfile.write(addr,data)}
  io.rs1Data := read(io.rs1,p.regFileReadKind)
  io.rs2Data := read(io.rs2,p.regFileReadKind)

  when(io.write && io.rd =/= 0){
    write(io.rd,io.data)
  }
}
