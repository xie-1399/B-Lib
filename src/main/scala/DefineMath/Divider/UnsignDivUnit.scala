package DefineMath.Divider

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._

/*
unsigned div unit use it the detail is https://blog.csdn.net/weixin_43813325/article/details/121399973
cycles : nWidth cycles
*/

case class UnsignDivCmd[T<:Data](nWidth:Int,dWidth:Int,contextType:T) extends Bundle{
  val numerator = UInt(nWidth bits)
  val denominator = UInt(dWidth bits)
  val context = cloneOf(contextType)
}

case class UnsignDivRsp[T<:Data](nWidth:Int,dWidth:Int,contextType:T) extends Bundle{
  val quotient = UInt(nWidth bits)
  val remainder = UInt(dWidth bits)
  val error = Bool()
  val context = cloneOf(contextType)
}

//can set context as cycles counter
class UnsignDivUnit[T<:Data](nWidth:Int,dWidth:Int,storeDenominator : Boolean,contextType:T = NoData()) extends PrefixComponent {
  val io = new Bundle{
    val flush = in Bool()
    val cmd = slave Stream(UnsignDivCmd(nWidth, dWidth, contextType))
    val rsp = master Stream(UnsignDivRsp(nWidth, dWidth, contextType))
  }
  val done = RegInit(True)
  val waitRsp = RegInit(False)
  val context = if(storeDenominator)Reg(contextType) else io.cmd.context
  val denominator = if(storeDenominator)Reg(UInt(dWidth bits)) else io.cmd.denominator
  val numerator = Reg(UInt(nWidth bits))
  val remainder = Reg(UInt(dWidth bits))
  val counter = Counter(nWidth)
  val remainderShifted = (remainder ## numerator.msb).asUInt
  val remainderMinusDenominator = remainderShifted - denominator

  //init the out put bundle
  io.cmd.ready := False
  io.rsp.valid := waitRsp
  io.rsp.payload.quotient := numerator
  io.rsp.payload.remainder := remainder
  io.rsp.payload.context := context
  io.rsp.error := denominator === 0
  when(io.rsp.ready){
    waitRsp := False
  }

  //begin with the cmd request
  when(done){
    when(!waitRsp || io.rsp.ready){
      counter.clear()
      remainder := 0
      numerator := io.cmd.numerator
      done := !io.cmd.valid
      if(storeDenominator){
        denominator := io.cmd.denominator
        context := io.cmd.context
        io.cmd.ready := True
      }
    }
  }.otherwise{
    //key logic of the div
    counter.increment()
    remainder := remainderShifted.resized
    numerator := (numerator ## !remainderMinusDenominator.msb).asUInt.resized
    when(!remainderMinusDenominator.msb){
      remainder := remainderMinusDenominator.resized
    }
    when(counter.willOverflowIfInc){
      done := True
      waitRsp := True
      if(storeDenominator){
        io.cmd.ready := True
      }
    }
  }

  when(io.flush){
    done := True
    waitRsp := False
  }

}
