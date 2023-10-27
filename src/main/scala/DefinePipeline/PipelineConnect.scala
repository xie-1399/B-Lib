package DefinePipeline

/***************************************************************************************
 * Copyright (c) 2020-2021 Institute of Computing Technology, Chinese Academy of Sciences
 * Copyright (c) 2020-2021 Peng Cheng Laboratory
 *
 * XiangShan is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 *
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *
 * See the Mulan PSL v2 for more details.
 ***************************************************************************************/

/* the source code is at https://github.com/IntelLabs/riscv-vector/blob/main/src/main/scala/util/PipelineConnect.scala
*  this build a simple way to connect the pipeline component
*  see the ConnectExample show using it */
import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._

object PipelineConnect {

  class PipelineConnectModule[T<:Data](gen:HardType[T]) extends PrefixComponent{
    val io = new Bundle{
      val dataIn = slave Stream gen
      val dataOut = master Stream gen
      val rightOutfire = in Bool()
      val flush = in Bool()
      val block = in Bool()
    }

    val valid = RegInit(False).setName("pipeline_valid")
    val leftFire = io.dataIn.valid && !io.block && io.dataOut.ready
    val payloadBits = RegNextWhen(io.dataIn.payload.asBits,leftFire).init(0)
    when(io.rightOutfire){
      valid := False
    }
    when(leftFire){
      valid := True
    }
    when(io.flush) {
      valid := False
    }
    io.dataIn.ready := io.dataOut.ready && !io.block

    io.dataOut.payload.assignFromBits(payloadBits)
    io.dataOut.valid := valid
  }

  def apply[T<:Data](left:Stream[T],right:Stream[T],rightOutFire:Bool,flush:Bool,block:Bool = False,moduleName:Option[String] = None) = {
    assert(left.payloadType.getBitsWidth == right.payloadType.getBitsWidth)
    val pipelineConnect = new PipelineConnectModule[T](left.payloadType)
    if(moduleName.nonEmpty) pipelineConnect.setName(moduleName.get)

    pipelineConnect.io.dataIn <> left
    pipelineConnect.io.block := block
    pipelineConnect.io.flush := flush
    pipelineConnect.io.rightOutfire := rightOutFire
    pipelineConnect.io.dataOut <> right
  }

}


