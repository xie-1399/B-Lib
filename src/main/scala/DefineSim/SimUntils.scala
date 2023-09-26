package DefineSim

import scala.collection.mutable._
import scala.util.Random

/*
here set some simulation function until for the spinal sim
*/

object SimUntils {

  def GenRandomList(base:Int,offset:Int,num:Int,log:Boolean = false,prefix:String = null):ArrayBuffer[BigInt] = {
    val res = ArrayBuffer[BigInt]()
    for(idx <- 0 until num){
      val value = Random.nextInt(offset) + base
      res += BigInt(value)
    }
    if(log){print(prefix); res.foreach(ele => print(s"$ele" + "\t"));print("\n")}
    res
  }

}
