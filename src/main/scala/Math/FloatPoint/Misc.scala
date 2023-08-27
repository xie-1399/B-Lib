package lib.Math.FloatPoint

import java.lang.Float._
import java.lang.Double._

//add FP32 and FP64 Misc
object Misc{
  //convert type to bit
  def asBits[T](content: T): Long = {
    val value = content.getClass.getSimpleName match {
      case "Float" => floatToIntBits(content.asInstanceOf[Float]) & 0x00000000ffffffffL
      case "Double" => doubleToLongBits(content.asInstanceOf[Double])
      case _ => {println("No implement error");0l}
    }
    value
  }
  //get signal and exp and mant part
  def signal[T](content: T, expWidth: Int, mantWidth: Int): Long = {
    val value = content.getClass.getSimpleName match {
      case "Float" => asBits[Float](content.asInstanceOf[Float]) >> (expWidth + mantWidth)
      case "Double" => asBits[Double](content.asInstanceOf[Double]) >> (expWidth + mantWidth)
      case _ => {println("No implement error");0l}
    }
    value
  }

  def exp[T](content:T,mantWidth: Int,expOffset:Long) = {
    val value = content.getClass.getSimpleName match {
      case "Float" => (asBits[Float](content.asInstanceOf[Float]) >> mantWidth) & expOffset
      case "Double" => (asBits[Double](content.asInstanceOf[Double]) >> mantWidth) & expOffset
      case _ => {println("No implement error");0l}
    }
    value
  }

  def mant[T](content: T, mantOffset: Long) = {
    val value = content.getClass.getSimpleName match {
      case "Float" => asBits[Float](content.asInstanceOf[Float]) & mantOffset
      case "Double" => asBits[Double](content.asInstanceOf[Double]) & mantOffset
      case _ => {println("No implement error"); 0l}
    }
    value
  }
  //Denormal
  def isDenormal[T](content:T,mantWidth: Int,expOffset:Long,mantOffset: Long):Boolean = {
    val value = content.getClass.getSimpleName match {
      case "Float" => exp(content.asInstanceOf[Float],mantWidth, expOffset) == 0 && mant(content.asInstanceOf[Float],mantOffset) != 0
      case "Double" => exp(content.asInstanceOf[Double],mantWidth, expOffset) == 0 && mant(content.asInstanceOf[Double],mantOffset) != 0
      case _ => {println("No implement error"); false}
    }
    value
  }
  def isZero[T](content:T,mantWidth: Int,expOffset:Long,mantOffset: Long): Boolean = {
    val value = content.getClass.getSimpleName match {
      case "Float" => exp(content.asInstanceOf[Float],mantWidth, expOffset) == 0 && mant(content.asInstanceOf[Float],mantOffset) == 0
      case "Double" => exp(content.asInstanceOf[Double],mantWidth, expOffset) == 0 && mant(content.asInstanceOf[Double],mantOffset) == 0
      case _ => {println("No implement error"); false}
    }
    value
  }

  def isNan[T](content: T): Boolean = {
    val value = content.getClass.getSimpleName match {
      case "Float" => content.asInstanceOf[Float].isNaN
      case "Double" => content.asInstanceOf[Double].isNaN
      case _ => {println("No implement error"); false}
    }
    value
  }

  def isInfinite[T](content: T): Boolean = {
    val value = content.getClass.getSimpleName match {
      case "Float" => content.asInstanceOf[Float].isInfinite
      case "Double" => content.asInstanceOf[Double].isInfinite
      case _ => {println("No implement error"); false}
    }
    value
  }

  def isRegular[T](content: T,mantWidth: Int,expOffset:Long,mantOffset: Long): Boolean = {
    val value = content.getClass.getSimpleName match {
      case "Float" => !isInfinite(content.asInstanceOf[Float]) && !isNan(content.asInstanceOf[Float]) &&
        !isDenormal(content.asInstanceOf[Float],mantWidth,expOffset, mantOffset)
      case "Double" => !content.asInstanceOf[Double].isInfinite && !content.asInstanceOf[Double].isNaN &&
        !isDenormal(content.asInstanceOf[Double],mantWidth,expOffset, mantOffset)
      case _ => {println("No implement error"); false}
    }
    value
  }

  //show formal type
  def print[T](content:T,expWidth:Int,mantWidth:Int,expValue:Long,mantValue:Long,signalValue:Long): Unit = {
    if(content.isInstanceOf[Float] || content.isInstanceOf[Double]){
      //float
      printf("%d ", signalValue)
      var i = expWidth - 1
      while (i >= 0) {
        printf("%d", (expValue >> i) & 1)
        i -= 1
      }
      printf(" ")
      i = mantWidth - 1
      while (i >= 0) {
        printf("%d", (mantValue >> i) & 1)
        i -= 1
      }
    }
    else {
      println("No implement error")
    }

  }
}




