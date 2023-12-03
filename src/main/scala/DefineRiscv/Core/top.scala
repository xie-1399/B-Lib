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

package DefineRiscv.Core
import DefinePipeline.PipelineConnect
import DefineRiscv.Core.frontend._
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._

class top(p:coreParameters) extends PrefixComponent{

  val io = new Bundle{
    val halt = in Bool()
    val flush = in Bool()
  }

  val fetch = new Fetch(p)
  val decode = new Decode(p)
  val regfile = new Regfile(p)

  val pipeline = PipelineConnect(fetch.io.fetchOut,decode.io.decodeIn,True,io.flush,io.halt)

  /* connect regfile and decode */
  decode.io.rs1Data := regfile.io.rs1Data
  decode.io.rs2Data := regfile.io.rs2Data
  regfile.io.rs1 := decode.io.rs1
  regfile.io.rs2 := decode.io.rs2

  decode.io.decodeOut.ready := True

}

object top extends App{
  val rtl = new RtlConfig().GenRTL(new top(coreParameters()))
}