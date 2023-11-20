package DefineMem.Cache

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba4.axi._
import DefineSim.SimUntils._
import spinal.core.sim._
import DefineSim.Logger._

/* the RCache is a read-only Cache and get more learning in the memory
*  when the cache hit it will get the data withIn one cycle
*  this is a block cache and the cmd will block if not hit value
* Todo test about the position */

/**
 * the parameters of the ReadOnly Cache
 * @param cacheSize the cache size is counter by byte
 * @param bytePerLine the cache line bytes
 * @param wayCount
 * @param addressWidth
 * @param cmdDataWidth
 * @param memDataWidth
 * @param catchIllegalAccess
 * @param byPass if by pass will send to bus and to the memory
 * @param flushIt
 * @param busDefault the axi4 config can be default
 * @param preResetFlush
 * @param bankWidthReduce show whether the cache mem width is the cache line bits width
 * @param WhiteBox
 */
case class RCacheConfig(cacheSize:Int,
                       bytePerLine:Int,
                       wayCount:Int,
                       addressWidth:Int,
                       cmdDataWidth:Int,
                       memDataWidth:Int,
                       catchIllegalAccess:Boolean = true,
                       byPass:Boolean = false,
                       flushIt:Boolean = true,
                       busDefault:Boolean = false,
                       preResetFlush:Boolean = true,
                       bankWidthReduce:Boolean = true,
                       WhiteBox:Boolean = false,
                      ){
  def burstSize = bytePerLine * 8 / memDataWidth /* the bus will trans one cache line spend cycles */
  def catchSomething = catchIllegalAccess
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
  def tagRange = addressWidth - 1 downto log2Up(wayLineCount * bytePerLine)
  def lineRange = tagRange.low - 1 downto log2Up(bytePerLine)

  /* for reduce the bank width using word */
  def bytePerWord = memDataWidth / 8
  val wordRange = log2Up(bytePerLine) -1 downto log2Up(bytePerWord)
  val wordCount = tagRange.low - 1 downto log2Up(bytePerWord)
  val byteRange = wordRange.low - 1 downto 0
}


class RCache(p:RCacheConfig) extends PrefixComponent{
  import p._

  val io = new Bundle{
    val flush = ifGen(flushIt){in Bool()}
    val driver = slave(RCacheDriverBus(p))
    val mem = master(RCacheMemBus(p))
  }

  case class lineTag() extends Bundle{
    val valid = Bool()
    val tag = UInt(tagRange.length bits)
  }

  val bankCount = wayCount
  val bankDepth = if(bankWidthReduce) cacheSize * 8 / wayCount / memDataWidth else cacheSize / bytePerLine / wayCount
  io.driver.cmd.ready := False
  /* sep the banks from ways */
  val bankWidth = if(bankWidthReduce) memDataWidth else bytePerLine * 8
  val banks = Seq.fill(wayCount)(Mem(Bits(bankWidth bits),bankDepth))
  val wayRandom = CounterFreeRun(p.wayCount)

  val ways = Seq.fill(wayCount)(
    new Area {
      val tags = Mem(lineTag(),wayLineCount)
      if(preResetFlush) tags.initBigInt(List.fill(wayLineCount)(BigInt(0)))
    }
  )
  val alignError = False

  val catchalignError = ifGen(catchIllegalAccess) {
    new Area{
      when(io.driver.cmd.valid){
        alignError := io.driver.cmd.physicalAddress(1 downto 0).asBits =/= B"00"
      }
    }
  }

  val readIdx = if(bankWidthReduce) lineRange.high downto wordRange.low else lineRange
  val HitIt = new Area{

    val read = new Area {

      val banksValue = for(bank <- banks) yield new Area {
        val dataMem = bank.readSync(io.driver.cmd.physicalAddress(readIdx),io.driver.cmd.valid)
        val data = if(bankWidthReduce) dataMem else dataMem.subdivideIn(memDataWidth bits)(io.driver.cmd.physicalAddress(wordRange))
      }
      val waysValue = for(way <- ways) yield new Area {
        val tag = way.tags.readAsync(io.driver.cmd.physicalAddress(lineRange))
      }
    }
    val hits = read.waysValue.map(way => way.tag.valid && way.tag.tag === io.driver.cmd.physicalAddress(tagRange))
    val valid = Cat(hits).orR
    val wayId = OHToUInt(hits)
    val data = read.banksValue.map(_.data).read(wayId)
    val cacheMiss = io.driver.cmd.valid && !valid
    when(valid){
      io.driver.cmd.ready := True
    }
  }

  val lineLoader = new Area{
    val flushArea = ifGen(flushIt){
      new Area{
        val flushtag = lineTag()
        val flushDone = RegInit(False)
        val flushCount = Counter(wayLineCount)
        flushtag.valid := False
        flushtag.tag := 0
        when(io.flush){
          /* only flush the valid position is enough */
          io.driver.cmd.ready := False
          val flush = for(way <- ways) yield new Area{
            way.tags.write(flushCount,flushtag)
          }
          flushCount.increment()
        }
        flushDone := flushCount.willOverflow
      }
    }

    val memCmd = new Area{
      val busy = RegInit(False)
      when(HitIt.cacheMiss){
        busy := True
      }
      io.mem.cmd.address := io.driver.cmd.physicalAddress
      io.mem.cmd.size := bytePerLine / memDataWidth - 1
      io.mem.cmd.valid := busy
      val fillTag = lineTag()
      fillTag.valid := False
      fillTag.tag := 0
      val addr = RegNextWhen(io.driver.cmd.payload.physicalAddress,io.driver.cmd.isStall)
      val writeCounter = Counter(bytePerLine / memDataWidth)
      // Todo how to choose the random way
      when(io.mem.rsp.valid){
        fillTag.valid := True
        fillTag.tag := addr(tagRange)
        ways(0).tags.write(addr(lineRange),fillTag)
        banks(0).write(addr(readIdx) + writeCounter,io.mem.rsp.data)
        writeCounter.increment()
      }
    }

  }

  io.driver.rsp.valid := RegNext(io.driver.cmd.fire)
  io.driver.rsp.data := HitIt.data
  io.driver.rsp.cacheMiss := HitIt.cacheMiss
  io.driver.rsp.error := alignError


  val whiteBox = ifGen(WhiteBox) {
    new Area {
      def logit() = {
        /* give the cache config information as follows */
        val report = CreateloggerFile(logpath = "src/main/scala/DefineMem/Cache/config.log",clear = true)
        report.write(s"cache size : ${cacheSize} Byte\n")
        report.write(s"way: ${wayCount}\n")
        report.write(s"tagRange: ${tagRange.start -> tagRange.end}\n")
        report.write(s"lineRange: ${lineRange.start -> lineRange.end}\n")
        report.write(s"wordRange: ${wordRange.start -> wordRange.end}\n")
        report.write(s"reduce the bank width : ${bankWidthReduce}")
        report.close()
      }
      /* the banks and value can set content */
      for(idx <- 0 until wayCount){
        banks(idx) simPublic()
        ways(idx).tags simPublic()
      }
      val bankIndex = RegNextWhen(io.driver.cmd.physicalAddress(readIdx),io.driver.cmd.fire)
      val lineIndex = RegNextWhen(io.driver.cmd.physicalAddress(lineRange),io.driver.cmd.fire)
      val tagValue = RegNextWhen(io.driver.cmd.physicalAddress(tagRange),io.driver.cmd.fire)

      /* here set a write enable area and debug write about it */
      val writeDebug = new Area{
        val writeEnable = RegInit(False).allowUnsetRegToAvoidLatch()
        val tagline = Reg(lineTag())
        tagline.valid := False
        tagline.tag := 0
        val addr = Reg(UInt(p.addressWidth bits)).init(0).allowUnsetRegToAvoidLatch()
        when(writeEnable){
          tagline.valid := True
          tagline.tag := addr(tagRange)
        }
        /* write the way should be random */
        ways(0).tags.write(RegNext(addr(lineRange)),tagline,enable = RegNext(writeEnable))
      }

      val sim = new Area{
        writeDebug.writeEnable.simPublic()
        writeDebug.addr.simPublic()
        bankIndex.simPublic()
        lineIndex.simPublic()
        tagValue.simPublic()
        lineLoader.flushArea.flushDone.simPublic()
      }

      logit()
    }
  }
}


object RCache extends App{
  val rtl = new RtlConfig().GenRTL(new RCache(RCacheConfig(cacheSize = (32 KiB).toInt, bytePerLine = 32, wayCount = 4, cmdDataWidth = 32, memDataWidth = 32,
    addressWidth = 32, WhiteBox = true)))
}
