package DefineMem.Cache

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._
import DefineSim.SimUntils._
import spinal.core.sim._

/* the RCache is a read-only Cache and get more learning in the memory
*  when the cache hit it will get the data withIn one cycle
* Todo test about the position */

/**
 * the parameters of the ReadOnly Cache
 * @param cacheSize the cache size is counter by byte
 * @param bytePerLine
 * @param wayCount
 * @param addressWidth
 * @param cmdDataWidth
 * @param memDataWidth
 * @param catchIllegalAccess
 * @param catchAccessFault
 * @param byPass if by pass will send to bus and to the memory
 * @param flushIt
 * @param busDefault the axi4 config can be default
 * @param WhiteBox
 */
case class RCacheConfig(cacheSize:Int,
                       bytePerLine:Int,
                       wayCount:Int,
                       addressWidth:Int,
                       cmdDataWidth:Int,
                       memDataWidth:Int,
                       catchIllegalAccess:Boolean = true,
                       catchAccessFault:Boolean = true,
                       byPass:Boolean = false,
                       flushIt:Boolean = true,
                       busDefault:Boolean = false,
                       preResetFlush:Boolean = true,
                       WhiteBox:Boolean = false
                      ){
  def burstSize = bytePerLine * 8 / memDataWidth /* the bus will trans one cache line spend cycles */
  def catchSomething = catchAccessFault && catchIllegalAccess
  /* the read size should be kept */
  def getAxi4Config() = Axi4Config(
    addressWidth = addressWidth,
    dataWidth = memDataWidth,
    useId = false,
    useRegion = false,
    useLock = false,
    useQos = false
  )
  /* this is enough to find the row index of the RCache */
  def lineWidth = 8 * bytePerLine
  def lineCount = cacheSize / bytePerLine
  def wayLineCount = lineCount / wayCount
  val tagRange = addressWidth - 1 downto log2Up(wayLineCount * bytePerLine)
  val lineRange = tagRange.low - 1 downto log2Up(bytePerLine)
  val byteRange = log2Up(bytePerLine) - 1 downto 0
}


class RCache(p:RCacheConfig) extends PrefixComponent{
  import p._

  val io = new Bundle{
    val flush = in Bool()
    val driver = slave(RCacheDriverBus(p))
  }

  case class lineTag() extends Bundle{
    val valid = Bool()
    val error = Bool() /* when the error happens pass it */
    val tag = UInt(tagRange.length bits)
  }

  val bankCount = wayCount
  val bankDepth = cacheSize / wayCount * 8 / memDataWidth
  io.driver.cmd.ready := !io.flush  /* show */
  /* sep the banks from ways */
  val banks = Seq.fill(wayCount)(Mem(Bits(memDataWidth bits),bankDepth))
  val ways = Seq.fill(wayCount)(
    new Area {
      val tags = Mem(lineTag(),wayLineCount)
      if(preResetFlush) tags.initBigInt(List.fill(wayLineCount)(BigInt(0)))
    }
  )

  val HitIt = new Area{

    val read = new Area {
      val banksValue = for(bank <- banks) yield new Area {
        val data = bank.readSync(io.driver.cmd.physicalAddress(lineRange),io.driver.cmd.fire)
      }
      val waysValue = for(way <- ways) yield new Area {
        val tag = way.tags.readAsync(io.driver.cmd.physicalAddress(tagRange))
      }
    }
    val hits = read.waysValue.map(way => way.tag.valid && way.tag.tag === io.driver.cmd.physicalAddress(tagRange))
    val valid = Cat(hits).orR
    val wayId = OHToUInt(hits)
    val data = read.banksValue.map(_.data).read(wayId)
    val error = read.waysValue.map(_.tag.error).read(wayId)
    val cacheMiss = io.driver.cmd.fire && !valid
  }

  val lineLoader = new Area{
    // reload the cache
  }
  io.driver.rsp.valid := RegNext(HitIt.valid)
  io.driver.rsp.data := HitIt.data
  io.driver.rsp.cacheMiss := HitIt.cacheMiss

  val whiteBox = ifGen(WhiteBox){
    def logit() = {
      /* give the cache config information as follows */
    }
    /* the banks and value can set content */
    for(idx <- 0 until wayCount){
      banks(idx) simPublic()
      ways(idx).tags simPublic()
      logit()
    }
  }
}




object RCache extends App{
  val rtl = new RtlConfig().GenRTL(new RCache(RCacheConfig(cacheSize = 4096, bytePerLine = 32, wayCount = 4, cmdDataWidth = 32, memDataWidth = 32,
    addressWidth = 32, WhiteBox = true)))
}
