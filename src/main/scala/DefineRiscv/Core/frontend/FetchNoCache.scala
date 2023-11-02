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
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import DefineRiscv.Core.coreParameters
import spinal.core.sim.SimPublic
import spinal.lib.bus.amba4.axi.Axi4ReadOnly
import spinal.core.sim._
/* just trans the request to the axi and use the memory sim */

class FetchNoCache(p:coreParameters) extends PrefixComponent{
  val io = new Bundle{
    val bus = master (Axi4ReadOnly(p.SimpleMemoryibusConfig))
  }
  val fetch = new Fetch(p)
  fetch.io.flush:= False
  fetch.io.halt := False
  fetch.io.pcLoad.valid := False
  fetch.io.pcLoad.payload := 0
  fetch.io.fetchOut.ready := True
  fetch.io.fetchBus.toAxi4ReadOnly() >> io.bus

  val whiteBox = ifGen(p.whiteBox){
    fetch.io.halt simPublic()
    fetch.io.pcLoad simPublic()
  }
}

