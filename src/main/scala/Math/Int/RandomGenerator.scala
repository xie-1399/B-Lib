package lib.Math.Int
import scala.util.Random
import spinal.core._
import spinal.lib._

//this is about how to generator random Int Value Component

class RandomGenerator(Width:Int) extends Component {
  val io = new Bundle{
    val random = out(Bits(Width bits))
    val valid = in Bool() //when valid get a new value
  }
  noIoPrefix()
  //get a random value
  val randomValue = RandomNumbers.randInt(0x7fffffff)
  val regValue = RegInit(B(randomValue , 96 bits))
  regValue(95 downto 32) := regValue(63 downto 0)
  for( i <- 0 until 32){
    regValue(31 - i) := regValue(95 - i) ^ regValue(93 - i) ^ regValue(48 - i) ^ regValue(46 - i)
  }
  io.random := regValue(31 downto 0)
}

object RandomNumbers{
  val randomNumbers = new Random(seed = 42)
  def randInt(value:Int) = randomNumbers.nextInt(value) // using Int -> 32 bits
}