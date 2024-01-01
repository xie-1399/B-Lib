package DefineMem

import spinal.core._
import spinal.lib._
import SRAM.DualPort._

object SRAMMemory{
  def MEM_TYPE = 1
}

class CustomMemory[T <: Data](val wordType: HardType[T],
                              val wordCount: Int,
                              val filepath: String,
                              val useDualPort: Boolean,
                              val withBitWrite: Boolean,
                              val Multiplexier: Int) extends Bundle {
  import SRAMMemory._
  val FPGAMem = MEM_TYPE == 0 generate Mem(wordType, wordCount)

  case class MemWriteCmdWithMask[T <: Data](customMemory: CustomMemory[T], maskWidth: Int) extends Bundle {
    val address = UInt(log2Up(customMemory.wordCount) bits)
    val data = customMemory.wordType()
    val mask = Bits(maskWidth bits)
  }

  case class MemReadPort[T <: Data](dataType: T, addressWidth: Int) extends Bundle with IMasterSlave {
    val cmd = Flow(UInt(addressWidth bit))
    val rsp = cloneOf(dataType)

    override def asMaster(): Unit = {
      master(cmd)
      in(rsp)
    }

    def bypass(writeLast: Flow[MemWriteCmd[T]]): Unit = new Composite(this, "bypass", true) {
      val cmdLast = RegNext(cmd.payload)
      val hit = cmdLast === writeLast.address && writeLast.valid
      when(hit) {
        rsp := writeLast.data
      }
    }
  }
  def renameIt() = {
    val Dual = if (useDualPort) "D" else "2"
    val Depth = wordCount / Multiplexier
    val dataWidth = wordType.getBitsWidth
    val end = if (withBitWrite) "_BW" else ""
    val name = "S55NLLG" + Dual + "PH" + "_X" + s"${Depth}" + s"Y${Multiplexier}" + s"D${dataWidth}" + end
    name
  }

  val SRAM = MEM_TYPE == 1 generate new MyRamDor2PH(
    Bits = wordType.getBitsWidth,
    Word_Depth = wordCount,
    Add_Width = log2Up(wordCount),
    AClockDomain = this.getComponent().clockDomain,
    BClockDomain = this.getComponent().clockDomain,
    path = filepath,
    useDualPort = useDualPort,
    withBitWrite = withBitWrite,
    Multiplexier = Multiplexier
  ).setDefinitionName(renameIt())

  //withBitWrite generate SRAM.io.BWENB.assignDontCare()
  def maskExpend(initLength: Int, resultLength: Int, initMask: Bits): Bits = {
    assert(initLength <= resultLength || resultLength % initLength == 0)
    val resultMask = Bits(resultLength bits)
    for (i <- 0 until initLength) {
      when(initMask(i)) {
        resultMask((resultLength/initLength * (i + 1) - 1) downto resultLength/initLength * i) := (default -> true)
      } otherwise {
        resultMask((resultLength/initLength * (i + 1) - 1) downto resultLength/initLength * i) := (default -> false)
      }
    }
    resultMask
  }

  def readSync(address: UInt, enable: Bool = null, readUnderWrite: ReadUnderWritePolicy = dontCare, clockCrossing: Boolean = false): T = {
    val readWord = wordType()
    val memAccess = MEM_TYPE match {
      case 0 => {
        readWord := FPGAMem.readSync(address, enable, readUnderWrite, clockCrossing)
      }
      case 1 => {
        SRAM.io.AA := address
        SRAM.io.CENA := !enable
        readWord.assignFromBits(SRAM.io.QA)
        useDualPort generate new Area {
          SRAM.io.CENB := False
        }
      }
    }
    readWord
  }

  def write(address: UInt, data: T, enable: Bool = null, mask: Bits = null): Unit = {
    val memAccess = MEM_TYPE match {
      case 0 => {
        FPGAMem.write(address, data, enable, mask)
      }
      case 1 => {
        SRAM.io.AB := address
        SRAM.io.DB := B(data)
        (!useDualPort) generate new Area {
          SRAM.io.CENB := !enable
        }
        useDualPort generate new Area {
          SRAM.io.WENB := !enable
          SRAM.io.DA := B(0).resized
          SRAM.io.WENA := True
          withBitWrite generate SRAM.io.BWENA.setAll()
        }
        withBitWrite generate new Area {
          SRAM.io.BWENB := ~maskExpend(mask.getBitsWidth, data.getBitsWidth, mask)
        }
      }
    }
  }

  def writePortWithMask(maskWidth: Int): Flow[MemWriteCmdWithMask[T]] = {
    val ret = Flow(MemWriteCmdWithMask(this, maskWidth))
    val memAccess = MEM_TYPE match {
      case 0 => {
        FPGAMem.write(ret.address, ret.data, ret.valid, ret.mask)
      }
      case 1 => {
        write(ret.address, ret.data, ret.valid, ret.mask)
      }
    }
    ret
  }

  def readSyncPort(readUnderWrite: ReadUnderWritePolicy = dontCare, clockCrossing: Boolean = false): MemReadPort[T] = {
    val ret: MemReadPort[T] = MemReadPort(this.wordType(), log2Up(this.wordCount))
    val memAccess = MEM_TYPE match {
      case 0 => {
        ret.rsp := FPGAMem.readSync(ret.cmd.payload, ret.cmd.valid, readUnderWrite, clockCrossing)
      }
      case 1 => {
        SRAM.io.AA := ret.cmd.payload
        SRAM.io.CENA := ret.cmd.valid
        ret.rsp.assignFromBits(SRAM.io.QA)
        useDualPort generate new Area {
          SRAM.io.CENB := False
        }
      }
    }
    ret
  }
}