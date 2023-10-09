package DefineMem
import spinal.core._
import spinal.lib._
import DefineSim.SpinalSim._
import spinal.lib.bus.amba4.axi.Axi4Config
import DefineSim.Logger._
import spinal.core
import spinal.core.sim._

import scala.collection.mutable.ArrayBuffer
import scala.util.Random
/*
 the custom define Cache for using purpose for readonly or read and write
 Bus: Axi4 port: dual port
 write policy - write back or not
 read policy - just read one cache line for missing
*/

case class cacheConfig(cacheSize:Int,
                       bytePerLine:Int,
                       wayCount:Int,
                       addressWidth:Int,
                       cmdDataWidth:Int,
                       memDataWidth:Int,
                       readOnly:Boolean = false,
                       byPass:Boolean = false,
                       flushIt:Boolean = true,
                       WhiteBox:Boolean = false
                      ){
  val burstSize = bytePerLine * 8 / memDataWidth /* the burst size define the burst used to trans from memory */
  val burstLength = bytePerLine / (memDataWidth / 8) /* length is also the same as size*/
  /*set bus config here*/
  def getAxi4Config() = Axi4Config(addressWidth = addressWidth, dataWidth = memDataWidth, useId = false,
    useRegion = false, useBurst = true, useLock = false,useCache = true, useSize = true, useQos = false,
    useLen = true, useLast = true, useResp = false, useStrb = true)
}

/* the driver cmd can be write or read */
case class driveCmd(config:cacheConfig) extends Bundle{
  val address = UInt(config.addressWidth bits)
  val wr = Bool()
  val data = Bits(config.cmdDataWidth bits)
  val mask = Bits(config.cmdDataWidth / 8 bits)
  ifGen(config.byPass){
    val bypass = Bool()
  }
}

case class driveRsp(config: cacheConfig) extends Bundle{
  val address = UInt(config.addressWidth bits)
  val data = Bits(config.cmdDataWidth bits)
}

case class driverBus(config: cacheConfig) extends Bundle with IMasterSlave{
  val cmd = Stream(driveCmd(config))
  val rsp = Flow(driveRsp(config))

  override def asMaster(): Unit = {
    master(cmd)
    slave(rsp)
  }
}

/* the mem bus should trans with the cache */
case class MemCmd(config:cacheConfig) extends Bundle{
  val wr = Bool()
  val address = UInt(config.addressWidth bits)
  val data = Bits(config.memDataWidth bits)
  val mask = Bits(config.cmdDataWidth / 8 bits)
  val length = UInt(log2Up(config.burstLength + 1) bits)
}

case class MemRsp(config: cacheConfig) extends Bundle{
  val data = Bits(config.memDataWidth bits)
}

case class MemBus(config:cacheConfig) extends Bundle with IMasterSlave {
  val cmd = Stream(MemCmd(config))
  val rsp = Flow(MemRsp(config))

  override def asMaster(): Unit = {
    master(cmd)
    slave(rsp)
  }
  /* convert the cmd request to the bus bundle*/
  def toAxi4() = {

  }

  def toSimpleBus() = {

  }
}

/* flush bus can be used to flush the cache value*/
case class flushBus(Nodata:Boolean = true) extends Bundle with IMasterSlave {
  val cmd = ifGen(Nodata){Event} /* Stream trans with No data will be the event*/
  val rsp = Bool()

  override def asMaster(): Unit = {
    master port (cmd) /* maybe null with it */
    in(rsp)
  }
}



class CustomCache(config: cacheConfig,name:String = "DefaultCache") extends PrefixComponent(name = name){
  import config._
  assert(config.cmdDataWidth == config.memDataWidth,"the memory bus width should equal the cmd data width")
  val logContant = ArrayBuffer[String]()

  val io = new Bundle{
    val driver = slave(driverBus(config))
    val mem = master(MemBus(config))
    val flush = slave(flushBus())
  }
  /* define the cache information with config*/
  val wordWidth = Math.max(memDataWidth,32)
  val bytePerWord = wordWidth / 8
  val wayLineCount = cacheSize / bytePerLine / wayCount
  val wayWordCount = cacheSize / wayCount / bytePerWord
  val tagRange = addressWidth -1 downto (log2Up(bytePerLine) + log2Up(wayLineCount)) /*or just use * */
  val lineRange = tagRange.low - 1 downto log2Up(bytePerLine)
  /* use the byte to index or use the word to index*/
  val wordRange = log2Up(bytePerLine) -1 downto log2Up(bytePerWord)
  val wordCount = tagRange.low - 1 downto log2Up(bytePerWord)
  val byteRange = wordRange.low - 1 downto 0
  logContant += s"wayLineCount:${wayLineCount}, wayWordCount:${wayWordCount}"
  logContant += s"tagRange:{${tagRange.start} -> ${tagRange.end}}"
  logContant += s"lineRange:{${lineRange.start} -> ${lineRange.end}}"
  logContant += s"wordRange:{${wordRange.start} -> ${wordRange.end}}"
  logContant += s"byteRange:{${byteRange.start} -> ${byteRange.end}}"
  logContant += s"wayWordRange:{${wordCount.start} -> ${wordCount.end}}"

  val haltCmd = False  /* stop to generate the cmd request */

  case class LineInfo() extends Bundle{
    val valid = Bool()
    val address = UInt(tagRange.length bits)
    val used = ifGen(!config.readOnly){Bool()}
    val dirty = ifGen(!config.readOnly) {Bool()}
  }

  /* create cache memory as Here and sim public for it*/
  val ways = Array.fill(wayCount){new Area {
    val tags = Mem.fill(wayLineCount)(LineInfo()) simPublic()
    val datas = Mem.fill(wayWordCount)(Bits(wordWidth bits)) simPublic()
  }}

  /* whether the read is hit*/
  val Hit = new Area {
    val request = io.driver.cmd.haltWhen(haltCmd).m2sPipe()
    val waysHitValid = False
    val waysHitValue = Bits(wordWidth bits)
    waysHitValue := 0
    request.ready := io.driver.rsp.fire /* seems like it's blocked */

    val waysRead = for(way <- ways) yield new Area {
      val address = io.driver.cmd.address
      val tag = way.tags.readSync(address(lineRange))
      val data = way.datas.readSync(address(lineRange.high downto wordRange.low))
      when(tag.valid && tag.address === request.address(tagRange)) {
        waysHitValid := True && !request.wr
        waysHitValue := data
      }
    }
    io.driver.rsp.valid := request.valid && waysHitValid
    io.driver.rsp.data := waysHitValue
    io.driver.rsp.address := request.address
  }

  /* the line loader to fill the tag with data */
  val LineLoader = new Area{

    val loader = Stream(new Bundle{
      val addr = UInt(addressWidth bits)
    })

    io.mem.cmd.address := loader.addr(tagRange.high downto lineRange.low) @@ U(0,lineRange.low bit)
    val lineInfoWrite = LineInfo()
    lineInfoWrite.valid := Flush.flushCounter.msb
    lineInfoWrite.address := loader.addr(tagRange)
    when(loader.fire){
      ???
    }

  }

  /* flush the cache trans with the flush bus , flush line by line */
  val Flush = ifGen(config.flushIt) { new Area {
    val flushFromInterface = RegInit(False)
    val flushCounter = Reg(UInt(log2Up(wayLineCount) + 1 bits)) init(0)
    val flushlineInfo = LineInfo()
    flushlineInfo.valid := False
    flushlineInfo.address := U"0".resized
    ifGen(!config.readOnly) {flushlineInfo.used := False}
    ifGen(!config.readOnly) {flushlineInfo.dirty := False}

    when(!flushCounter.msb){
      /* should flush the cache at first flush the valid bit */
      haltCmd := True
      flushCounter := flushCounter + 1
      for(way <- ways) yield new Area{
        for(idx <- 0 until wayLineCount){
          way.tags.write(U(idx),flushlineInfo)
        }
      }
    }
    when(!RegNext(flushCounter.msb)){
      haltCmd := True
    }
    when(io.flush.cmd.valid){
      haltCmd := True
      when(io.flush.cmd.ready){
        flushCounter := 0
        flushFromInterface := True
      }
    }
    val flushRsp = if (flushIt) (flushCounter.msb.rise && flushFromInterface) else False
    io.flush.rsp := flushRsp /* show when is interface flushing */
    io.flush.cmd.ready := !(Hit.request.valid)
  }}

  report.GenReport(config,name,logContant)

}

object report{
  def GenReport(config: cacheConfig, name:String,others: ArrayBuffer[String]) = {
    logout(content = "parameters in the Cache as follow:", withTime = true, clear = true, project = name, showproject = true)
    logout(s"CacheSize:${config.cacheSize} Bytes", withTime = false)
    logout(s"BytePerLine:${config.bytePerLine}", withTime = false)
    logout(s"WayCount:${config.wayCount}", withTime = false)
    logout(s"Memory Data Width:${config.memDataWidth}", withTime = false)
    logout(s"Cmd Data Width:${config.cmdDataWidth}", withTime = false)
    logout(s"ReadOnly:${config.readOnly}", withTime = false)
    for (other <- others) {
      logout(other, withTime = false)
    }
  }
}



object CustomCache extends App{
  val config = cacheConfig(cacheSize = 4096, bytePerLine = 32, wayCount = 4, addressWidth = 32,
    cmdDataWidth = 32, memDataWidth = 32, readOnly = true)

  val rtl = new RtlConfig().GenRTL(top = new CustomCache(config,name = "InstructionCache"))
}

