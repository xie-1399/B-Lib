package DefineMem

import DefineSim.SpinalSim.PrefixComponent
import spinal.core
import spinal.core._
import spinal.lib._

/*
 * the spinal libs has Stream fifo
 * this file will make a more friendly use fifo
 * the fifo control is more simple to use
*/

class FIFO[T<:Data](gen:HardType[T],val entries:Int,withFlush:Boolean = false) extends PrefixComponent{

  val io = new Bundle{
    val enqueue = slave (Stream(gen))
    val dequeue = master (Stream(gen))
    val flush = withFlush.generate(in Bool())
    val flushValue = withFlush.generate(in (gen))
    val flushDone = withFlush.generate(out Bool())
  }

  val enq_ptr = Counter(entries).init(0)
  val deq_ptr = Counter(entries).init(0)

  val queue = Mem(gen,entries)
  if(withFlush){io.flushDone := RegNext(MemOperation.flush(io.flush,queue,io.flushValue))}
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

  if(withFlush) {when(io.flush) {io.enqueue.ready := False;io.dequeue.valid := False}}
}


object FIFO{
  def apply[T<: Data](gen:HardType[T],entries:Int) = {
    new FIFO(gen,entries)
  }

  def apply[T <: Data](gen: HardType[T], entries: Int,withFlush:Boolean) = {
    new FIFO(gen,entries,withFlush)
  }
}