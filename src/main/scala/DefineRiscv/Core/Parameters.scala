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
import frontend.TCMParameters
import spinal.lib.bus.amba4.axi.Axi4Config

case class coreParameters(resetValue:BigInt = 0x40000000l,
                          withRVC:Boolean = false,
                          ioRange:BigInt = 0x1,
                          itcmRange:BigInt = 0x4,
                          whiteBox:Boolean = false
                         ){
  def Xlen = 32
  def instructionWidth = 32

  def itcmParameters = TCMParameters(TCMBlock = 1,TCMDepth = 65536) /* config the itcm -> 1 * 64K*/

  def SimpleMemoryibusConfig = Axi4Config(addressWidth = 32,dataWidth = 32,useId = false,useBurst = false,
    useQos = false,useLock = false,useResp = false,useRegion = false,useCache = false,useProt = false)

}


