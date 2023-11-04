package DefineBus.Axi

import spinal.core._
import spinal.sim._
import spinal.core.sim._
import spinal.lib.bus.amba4.axi._
import spinal.lib.bus.amba4.axi.sim.{SparseMemory,AxiJob}
import scala.collection.mutable
import scala.util.Random
import spinal.lib._

/*
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
this part is likely AxiMemorySim but Readonly@@
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
*/


case class AxiReadonlyMemorySimConfig (
                                maxOutstandingReads  : Int = 8,
                                readResponseDelay    : Int = 0,
                                interruptProbability : Int = 0,
                                interruptMaxDelay    : Int = 0,
                                defaultBurstType     : Int = 1,
                                useAlteraBehavior    : Boolean = false
                              )


case class AxiReadonlyMemorysim(axi : Axi4ReadOnly, clockDomain : ClockDomain, config : AxiReadonlyMemorySimConfig) {
  val memory = SparseMemory()
  val pending_reads = new mutable.Queue[AxiJob]
  val threads = new mutable.Queue[SimThread]

  /** Bus word width in bytes */
  val busWordWidth = axi.config.dataWidth / 8
  val maxBurstSize = log2Up(busWordWidth)

  def newAxiJob(address: Long, burstLength: Int, burstSize: Int, burstType: Int, id: Long): AxiJob = {
    AxiJob(address, burstLength, burstSize, burstType, id)
  }

  def newAxiJob(ax: Axi4Ax): AxiJob = {
    newAxiJob(
      address = ax.addr.toLong,
      burstLength = getLen(ax),
      burstSize = getSizeAndCheck(ax),
      burstType = getBurst(ax),
      id = getId(ax)
    )
  }

  def start(): Unit = {
    threads.enqueue(fork {
      handleAr(axi.ar)
    })

    threads.enqueue(fork {
      handleR(axi.r)
    })
  }

  def stop(): Unit = {
    threads.map(f => f.terminate())
  }

  def reset(): Unit = {
    stop()
    pending_reads.clear()
    start()
  }

  def getLen(ax: Axi4Ax): Int = {
    if (ax.config.useLen) ax.len.toInt else 0
  }

  def getSize(ax: Axi4Ax): Int = {
    if (ax.config.useSize) ax.size.toInt else maxBurstSize
  }

  def getSizeAndCheck(ax: Axi4Ax): Int = {
    val burstSize = getSize(ax)
    assert(burstSize <= maxBurstSize)
    burstSize
  }

  def getId(ax: Axi4Ax): Long = {
    if (ax.config.useId) ax.id.toLong else 0L
  }

  def getBurst(ax: Axi4Ax): Int = {
    if (ax.config.useBurst) ax.burst.toInt else config.defaultBurstType
  }

  def setLast(r: Axi4R, last: Boolean): Unit = {
    if (r.config.useLast) {
      r.last #= last
    }
  }

  def handleAr(ar: Stream[Axi4Ar]): Unit = {
    println("Handling AXI4 Master read cmds...")
    ar.ready #= false

    while (true) {
      ar.ready #= true
      clockDomain.waitSamplingWhere(ar.valid.toBoolean)
      ar.ready #= false

      pending_reads += newAxiJob(ar.payload)

      if (pending_reads.length >= config.maxOutstandingReads)
        clockDomain.waitSamplingWhere(pending_reads.length < config.maxOutstandingReads)
    }
  }

  def handleR(r: Stream[Axi4R]): Unit = {
    println("Handling AXI4 Master read resp...")

    val random = Random
    r.valid #= false
    setLast(r.payload, false)
    while (true) {
      if (pending_reads.nonEmpty) {
        var job = pending_reads.front
        r.valid #= true

        var i = 0
        while (i <= job.burstLength) {
          if (config.interruptProbability > random.nextInt(100)) {
            r.valid #= false
            clockDomain.waitSampling(random.nextInt(config.interruptMaxDelay + 1))
            r.valid #= true
          }

          if (i == job.burstLength)
            setLast(r.payload, true)
          if (r.config.useId)
            r.payload.id #= job.id
          if (r.config.useResp)
            r.payload.resp #= 0

          r.payload.data #= memory.readBigInt(job.alignedBurstAddress(i, maxBurstSize), busWordWidth)
          clockDomain.waitSamplingWhere(r.ready.toBoolean)
          i = i + 1
        }
        r.valid #= false
        setLast(r.payload, false)
        pending_reads.dequeue()
      } else {
        clockDomain.waitSampling(1)
      }
    }
  }
}
