package lib.tools

import spinal.core.BitCount
import spinal.lib._

//review the binary system for sim or test
//just a simple tool more details about:https://spinalhdl.github.io/SpinalDoc-RTD/v1.8.0/SpinalHDL/Libraries/binarySystem.html#

object binarySystem {
  //convert String to BigInt
  def String2BigInt(s:String,system:Int) = {
    val systemList = List[Int](2,8,10,16)
    assert(systemList.contains(system),"only support 2 8 10 16 for string to bigint")
    val value = BigInt(s,system)
    value
  }

  //convert BigInt into a List of Boolean
  def BigInt2ListBoolean(value:BigInt,size:BitCount):List[Boolean] = {
    //about the BitCount -> like 4 bits
    assert(size.value >=0,"size must be bigger than 0")

    def bigInt2List(that:BigInt) : List[Boolean] = {
      if(that == 0) Nil
      else List(that.testBit(0)) ::: bigInt2List(that >> 1)  //see the last bit and recursive
    }

    def paddingList(l: List[Boolean], size: Int): List[Boolean] = {
      if (l.length == size) l
      else if (l.length > size) l.drop(l.length - size)
      else l ::: List.fill(size - l.length)(false)
    }

    var listBoolean = List[Boolean]()
    if(value < 0){
      listBoolean = bigInt2List((value.abs ^ ((BigInt(1) << size.value) - 1)) + 1)
    } else{
      listBoolean = bigInt2List(value)
    }
    paddingList(listBoolean,size.value)
  }

  // int / long / bigInt can work
  def toBinaryList(value:BigInt,align:Boolean = false,alignNum : Int = 0) = {
    val list = if(align) value.toBinInts(alignNum) else value.toBinInts
  }
}


