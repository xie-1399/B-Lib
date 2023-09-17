package DefineUntils.Switch

import DefineSim.SpinalSim.PrefixComponent
import spinal.lib._
import spinal.core._
import DefineUntils.OneHotOperation
import DefineSim.SpinalSim._

object Crossbar2x2 {
  def apply[T <: Data](sel: Bool, first: T, second: T): (T,T) = {
    (Mux(sel, first, second), Mux(!sel, first, second))
  }

  def apply[T <: SpinalEnum](sel: Bool, first: SpinalEnumElement[T], second: SpinalEnumElement[T]): (SpinalEnumCraft[T],SpinalEnumCraft[T]) = {
    (Mux(sel, first, second), Mux(!sel, first, second))
  }

  def apply[T <: SpinalEnum](sel: Bool, first: SpinalEnumCraft[T], second: SpinalEnumElement[T]): (SpinalEnumCraft[T],SpinalEnumCraft[T]) = {
    (Mux(sel, first, second), Mux(!sel, first, second))
  }

  def apply[T <: SpinalEnum](sel: Bool, first: SpinalEnumElement[T], second: SpinalEnumCraft[T]): (SpinalEnumCraft[T],SpinalEnumCraft[T]) = {
    (Mux(sel, first, second), Mux(!sel, first, second))
  }
}


object CrossbarResize{
  def apply[T<:Data](sel:Vec[Bool],data:Vec[T],size:Int): T = {
    require(sel.size == data.size)
    val mapping = for(idx <- 0 until size) yield idx
    val index = OneHotOperation.MappingBoolsOh2UInt(sel,mapping)
    data(index)
  }

}

class CrossbarExample extends PrefixComponent{
  val io = new Bundle{
    val sel = in Vec(Bool(),10)
    val data = in Vec(UInt(256 bits),10)
    val cmd = out(UInt(256 bits))
  }
  io.cmd := CrossbarResize(io.sel,io.data,size = 10)
}

object CrossbarExample extends App{
  val rtl = new RtlConfig().GenRTL(top = new CrossbarExample())
}