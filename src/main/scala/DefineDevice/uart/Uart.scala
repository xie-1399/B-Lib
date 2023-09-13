package DefineDevice.uart

import spinal.core._
import spinal.lib._

  /*
  for convent -> this file will contain all the uart files
  the source code is : https://github.com/SpinalHDL/SpinalHDL/tree/dev/lib/src/main/scala/spinal/lib/com/uart
 */

object UartStopType extends SpinalEnum(binarySequential){
  /*
  stop signal type(send the data late)
  */
  val ONE,TWO = newElement()
  def toBitCount(that:C):UInt = (that === ONE)? U"0" | U"1"
}

object UartParityType extends SpinalEnum(binarySequential){
  /* counter with the one bit*/
  val NONE,EVEN,ODD = newElement()
}


object UartCtrlTxState extends SpinalEnum{
  /*
  send the tx cmd machine state
  */
  val IDLE,START,DATA,PARITY,STOP = newElement()
}


case class UartInterface(ctsGen:Boolean = false , rtsGen:Boolean = false) extends Bundle with IMasterSlave{
  /*
  send / receive data or cmd signal
  */
  val txd = Bool()
  val rxd = Bool()
  val cts = ifGen(ctsGen) {Bool()}
  val rts = ifGen(rtsGen) {Bool()}
  override def asMaster(): Unit = {
    out(txd)
    in(rxd)
    out port(rts)  //send the cmd
    in port(cts)
  }
}

case class UartGen(dataWidthMax : Int = 8,
                   clockDividerWidth:Int = 20,
                   preSamplingSize:Int = 1,
                   samplingSize:Int = 5,
                   postSamplingSize:Int = 2,
                   ctsGen:Boolean = false,
                   rtsGen:Boolean = false){
  val rxSamplePerBit = preSamplingSize + samplingSize + postSamplingSize
}

 /*
  The UART FrameWork Config
 */
case class UartCtrlFrameConfig(gen:UartGen) extends Bundle{
  val dataLength = UInt(log2Up(gen.dataWidthMax) bits)
  val stop = UartStopType()
  val parity = UartParityType()
}

 /*
  the tx recevice the stream uart fram -> convert it to the txd (one bit by one bit)
 */

class UartCtrlTx(gen:UartGen) extends Component{
  import gen._


}
