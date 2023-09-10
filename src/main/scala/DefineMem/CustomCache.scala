package DefineMem
import spinal.core._
import spinal.lib._
import DefineSim.SpinalSim.PrefixComponent

/*

 the custom define Cache for using purpose for readonly or read and write
 write policy - write back or not
 read policy - just read one cacheline for missing
*/

case class CacheConfig()

class CustomCache(config: CacheConfig) extends PrefixComponent{

}
