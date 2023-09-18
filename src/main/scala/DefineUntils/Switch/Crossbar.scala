package DefineUntils.Switch

import DefineSim.SpinalSim.PrefixComponent
import spinal.lib._
import spinal.core._
import DefineSim.SpinalSim._

 /*
  use the id to define the crossbar choose logic
 */

object CrossbarResize{
  /* without fifo and use the id to define the final result*/
  def apply[T <: Data](data:Vec[T],size:Int,id:Vec[UInt]):Vec[T] = {
    require(id.size == data.size)
    require(log2Up(size) <= id.getBitsWidth)
    val res = cloneOf(data)
    for(idx <- 0 until size){
      res(idx) := data(id(idx))
    }
    res
  }

  def apply[T<:Data](data:Vec[T],size:Int,id:Vec[UInt],write:Bool):Vec[T] = {
    /* with the simple reg file -> the data will first write to the reg file*/
    require(id.size == data.size)
    require(log2Up(size) <= id.getBitsWidth)
    val res = data.clone()
    val fifo = Vec(Reg(data.dataType),size)
    when(write){
      for(idx <- 0 until size){
        fifo(idx) := data(idx)
      }
    }
    for(idx <- 0 until size){
      res(idx) := fifo(id(idx))
    }
    res
  }
}

class CrossbarConfig(size:Int,dataWidth:Int,idWidth:Int,withFifo:Boolean = false) extends PrefixComponent {
  val io = new Bundle{
    val id = in Vec(UInt(idWidth bits), size)
    val data = in Vec(UInt(dataWidth bits), size)
    val cmd = out Vec(UInt(dataWidth bits), size)
    val write = ifGen(withFifo){ in Bool()}
  }
  if(withFifo){
    io.cmd := CrossbarResize(io.data,size = 10,id = io.id,write = io.write)
  }else{
    io.cmd := CrossbarResize(io.data,size = 10,id = io.id)
  }
}

object CrossbarExample extends App{
  val rtl = new RtlConfig().GenRTL(top = new CrossbarConfig(size = 10,dataWidth = 256,idWidth = 4,withFifo = true))
}