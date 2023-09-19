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

  /* each device has a fifo with the bits combine */
  def apply[T<:Data](data:Vec[T],size:Int,id:Vec[UInt],write:Bool,Seperate:Boolean,depth:Int = 10):Vec[T] = {
    require(id.size == data.size)
    require(log2Up(size) <= id.getBitsWidth)
    val res = data.clone()
    val datafifo = Vec(Reg(data.dataType), depth)
    val idfifo = Vec(Reg(id.dataType),depth)

    val dataArray = Vec(Reg(datafifo.clone()), size)
    val idArray = Vec(Reg(idfifo.clone()), size)

    val lineCounter = Counter(depth).init(0)

    when(write) {
      for (idx <- 0 until size) {
        dataArray(idx)(lineCounter) := data(idx)
        idArray(idx)(lineCounter) := id(idx)
      }
      lineCounter.increment()
    }

    for(idx <- 0 until size){
      res(idx) := dataArray(idArray(idx)(lineCounter.value - 1))(lineCounter.value - 1)
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
    val cross = CrossbarResize(io.data,size = size,id = io.id,write = io.write,Seperate = true)
    io.cmd := cross
  }else{
    io.cmd := CrossbarResize(io.data,size = size,id = io.id)
  }
}

object CrossbarExample extends App{
  val rtl = new RtlConfig().GenRTL(top = new CrossbarConfig(size = 3,dataWidth = 128,idWidth = 2,withFifo = true),pruned = true)
}