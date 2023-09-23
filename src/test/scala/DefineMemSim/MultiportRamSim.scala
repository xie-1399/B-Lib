package DefineMemSim

import org.scalatest.funsuite.AnyFunSuite
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import DefineSim._
import DefineMem._

import scala.collection.mutable.ArrayBuffer
/*
 the simulation is about the multi port ram
*/

class MultiportRamSim extends AnyFunSuite{

  test("Mr1w") {
    SIMCFG(gtkFirst = true).compile {
      val dut = new MultiportRam.RamMr1w(Bits(8 bits),readPort = 4,depth = 16)
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)

        def init() = {
          dut.io.write.valid #= false
          for(read <- dut.io.reads){
            read.cmd.valid #= false
          }
        }

        def write(address:BigInt,data:BigInt): Unit = {
          dut.io.write.valid #= true
          dut.io.write.address #= address
          dut.io.write.data #= data
          dut.clockDomain.waitSampling()
        }

        def read(addresslist:Seq[BigInt]) = {
          for((address,index) <- addresslist.zipWithIndex){
            dut.io.write.valid #= false
            dut.io.reads(index).cmd.payload #= address
          }
          dut.clockDomain.waitSampling()
        }
        init()
        write(0,10)
        write(1,2)
        write(2,3)
        write(3,4)
        read(Seq(0,1,2,3))
        Logger.apply(dut.io.reads(0).rsp,signal = false,bitWidth = 8)
        Logger.apply(dut.io.reads(1).rsp,signal = false,bitWidth = 8)
        Logger.apply(dut.io.reads(2).rsp,signal = false,bitWidth = 8)
        Logger.apply(dut.io.reads(3).rsp,signal = false,bitWidth = 8)
    }
  }

  test("MrMw / Xor"){
    SIMCFG(gtkFirst = true).compile {
      val dut = new MultiportRam.RamMrMw(Bits(8 bits), readPort = 4,writePort = 4, depth = 16,UseXor = false,ReadAsync = false)
      dut
    }.doSim {
      dut =>
        dut.clockDomain.forkStimulus(10)
        val valids = ArrayBuffer[Bool]()
        for ((read,write) <- (dut.io.reads,dut.io.writes).zipped){
          valids += read.cmd.valid
          valids += write.valid
        }
        SpinalSim.simInitValue(valids,boolean = true,bits = false,clockDomain = dut.clockDomain)

        def monitor(addresslist: List[BigInt], datalist: List[BigInt]): Unit = {
          for ((address, index) <- addresslist.zipWithIndex) {
            dut.io.reads(index).cmd.valid #= false
            dut.io.writes(index).valid #= true
            dut.io.writes(index).address #= address
            dut.io.writes(index).data #= datalist(index)
          }
          dut.clockDomain.waitSampling()
          for ((address, index) <- addresslist.zipWithIndex) {
            dut.io.reads(index).cmd.valid #= true
            dut.io.writes(index).valid #= false
            dut.io.reads(index).cmd.payload #= address
          }
          dut.clockDomain.waitSampling(2)

          /* show the value*/
          for(idx <- 0 until 4){
            Logger.apply(dut.io.reads(idx).rsp,signal = false,bitWidth = 8)
          }
        }

        val addresslist = List[BigInt](0,1,2,3)
        val datalist = List[BigInt](10,9,8,7)
        monitor(addresslist,datalist)
    }
  }
}
