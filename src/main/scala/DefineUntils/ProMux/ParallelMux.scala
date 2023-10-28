package DefineUntils.ProMux

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

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._

/* in fact the vec and seq extends same class */

object ParallelOperation{
  def apply[T](xs:Seq[T],func:(T,T) => T):T = {
    require(xs.nonEmpty)
    xs match {
      case Seq(a) => a
      case Seq(a,b) => func(a,b)
      case _ =>
        /* the drop and take will dive the seq into two parts */
        apply(Seq(apply(xs take xs.size / 2,func),apply(xs drop xs.size/2, func)),func)
    }
  }
}

/* get the mapping OR value from the vec */
object ParallelOR{
  def apply[T<:Data](xs:Seq[T]) = {
    ParallelOperation(xs,(a:T,b:T) => (a.asBits | b.asBits).as(cloneOf(xs.head)))
  }
  /* the same as balance Tree */
  def balanceTreeOR[T<:BitVector](xs:Seq[T]):T= {
    val res = new TraversableOncePimped(xs)
    res.reduceBalancedTree((opA,opB) => (opA.asBits | opB.asBits).as(cloneOf(xs.head)))
  }
}

/* get the mapping XOR value from the vec */
object ParallelXOR{
  def apply[T<:Data](xs:Seq[T]) = {
    ParallelOperation(xs,(a:T,b:T) => (a.asBits ^ b.asBits).as(cloneOf(xs.head)))
  }
  /* the same as balance Tree */
  def balanceTreeXOR[T<:BitVector](xs:Seq[T]):T= {
    val res = new TraversableOncePimped(xs)
    res.reduceBalancedTree((opA,opB) => (opA.asBits ^ opB.asBits).as(cloneOf(xs.head)))
  }
}

/* get the mapping And value from the vec */
object ParallelAnd{
  def apply[T<:Data](xs:Seq[T]) = {
    ParallelOperation(xs,(a:T,b:T) => (a.asBits & b.asBits).as(cloneOf(xs.head)))
  }
  /* the same as balance Tree */
  def balanceTreeAnd[T<:BitVector](xs:Seq[T]):T= {
    val res = new TraversableOncePimped(xs)
    res.reduceBalancedTree((opA,opB) => (opA.asBits & opB.asBits).as(cloneOf(xs.head)))
  }
}

/* get the mapping value from the vec */
object ParallelMux{ /* through the bools get the choose value*/
  def apply[T<:Data](states:Seq[(Bool,T)]) = {
    val xs = states.map{
      case (cond,x) => (Seq.fill(x.getBitsWidth)(cond).asBits() & x.asBits).as(cloneOf(states.head._2))
    }
    ParallelOR(xs)
  }
}

object ParallelLookUp {
  def apply[T<:Data](key: UInt, mapping:Seq[(UInt,T)]): T = {
    ParallelMux(mapping.map(m => (m._1===key) -> m._2)) /* get a new key-value pair */
  }
}

/* get the max or min value from the vec */
class ParallelMax {
  def apply(xs: Seq[UInt]): UInt = {
    ParallelOperation(xs, (a: UInt, b:UInt) => Mux(a > b, a, b))
  }
  def apply(xs: Seq[SInt]): SInt = {
    ParallelOperation(xs, (a: SInt, b: SInt) => Mux(a > b, a, b))
  }
}

class ParallelMin {
  def apply(xs: Seq[UInt]): UInt = {
    ParallelOperation(xs, (a: UInt, b: UInt) => Mux(a < b, a, b))
  }
  def apply(xs: Seq[SInt]): SInt = {
    ParallelOperation(xs, (a: SInt, b: SInt) => Mux(a < b, a, b))
  }
}

/* get the add res with extension */
class ParallelAdd{
  def apply(xs: Seq[UInt],signExtension:Boolean): UInt = {
    ParallelOperation(xs, (a: UInt, b: UInt) => if(signExtension) (a +^ b) else a + b)
  }

  def apply(xs: Seq[SInt],signExtension:Boolean): SInt = {
    ParallelOperation(xs, (a: SInt, b: SInt) => if(signExtension) (a +^ b) else a + b)
  }
}

// Todo simulation with it
object ParallelPriorityMux {
  def apply[T <: Data](states: Seq[(Bool, T)]): T = {
    ParallelOperation(states, (a: (Bool, T), b: (Bool, T)) => (a._1 || b._1, Mux(a._1, a._2, b._2)))._2
  }
  def apply[T <: Data](sel: Bits, states: Seq[T]): T = apply((0 until states.size).map(sel(_)), states)
  def apply[T <: Data](sel: Seq[Bool], states: Seq[T]): T = apply(sel zip states)
}

/* a simple example */
class ParallelExample extends PrefixComponent{
  val io = new Bundle{
    val seq = in Vec(Bits(10 bits),32)
  }
  val res1 = ParallelOR(io.seq)
  val res2 = ParallelOR.balanceTreeOR(io.seq)
}

object ParallelExample extends App{
  val rtl = new RtlConfig().GenRTL(top = new ParallelExample()) /* they are just the same*/
}
