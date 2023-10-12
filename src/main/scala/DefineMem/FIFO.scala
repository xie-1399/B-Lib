package DefineMem

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._

/*
 * the spinal libs has Stream fifo
 * this file will make a more friendly use fifo
 * the fifo control is more simple to use
*/

class FIFO[T<:Data](gen:HardType[T],val entries:Int,pass:Boolean = false,pipe:Boolean = false) extends PrefixComponent{

  val io = new Bundle{
    val enqueue = slave (Stream(gen))
    val dequeue = master (Stream(gen))
  }

  val enq_ptr = Counter(entries).init(0)
  val deq_ptr = Counter(entries).init(0)

  val queue = MemOperation(gen,entries)
  val full_empty = RegInit(False)
  io.dequeue.payload := queue.readAsync(deq_ptr)

  when(io.enqueue.fire){
    /* into the queue */
    queue.write(enq_ptr,io.enqueue.payload)
    enq_ptr.increment()
  }

  when(io.dequeue.fire){
    /* out the queue */
    deq_ptr.increment()
  }

  val empty = enq_ptr === deq_ptr && !full_empty
  val full = enq_ptr === deq_ptr && full_empty

  /* the key concept about the difference enqueue and dequeue */
  when(io.enqueue.fire =/= io.dequeue.fire){
    full_empty := io.enqueue.fire
  }

  io.enqueue.ready := !full
  io.dequeue.valid := !empty

}


object FIFO{
  def apply[T<: Data](gen:HardType[T],entries:Int) = {
    new FIFO(gen,entries)
  }

  def apply[T <: Data](gen: HardType[T], entries: Int,pass:Boolean,pipe:Boolean) = {
    new FIFO(gen, entries,pass,pipe)
  }
}