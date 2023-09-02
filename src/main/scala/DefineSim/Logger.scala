package DefineSim

import java.io._
import java.text._
import java.util._
import spinal.core._
import spinal.core.sim._

//create logfile in the Accelerator project

object Logger{
  /* the print format of the log value*/
  def apply(value:UInt,hex:Boolean = false) = {
    val res = if(hex) value.toLong.toHexString else value.toLong
    println(value.name + "   is  " + res)
  }

  def apply(value:Bool,binary:Boolean) = {
    val res = if(binary) value.toBoolean.toInt else value.toBoolean
    println(value.name + "   is  " + res)
  }

  def apply(value: SInt,binary:Boolean) = {
    val res = if(binary) value.toLong.toBinaryString else value.toLong
    println(value.name + "   is  " + res)
  }

  def apply(value:Bits,signal:Boolean,bitWidth:Int): Unit = {
    val res = if(signal){
      unsignedToSigned(value.toLong,bitWidth)
    }
    else {
      value.toLong
    }
    println(value.name + "   is  " + res)
  }

  // a convert to the signed number
  def unsignedToSigned(unsignedValue: Long, bitWidth: Int): Long = {
    val maxUnsignedValue = (1L << bitWidth) - 1L
    if (unsignedValue <= maxUnsignedValue / 2) {
      unsignedValue
    } else {
      unsignedValue - (maxUnsignedValue + 1)
    }
  }

  def CreateloggerFile(logpath:String = "./results.log",clear:Boolean = false) = {
    if(clear){
      val file = new File(logpath)
      file.delete()
    }
    val logger = new FileWriter(logpath,true)
    logger
  }

  def logout(content: String,project:String = null,showproject:Boolean = false,
             withTime:Boolean = true,logpath:String = "./results.log",clear:Boolean = false): Unit = {
    val logger = CreateloggerFile(logpath, clear)
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val cla = Calendar.getInstance()
    cla.setTimeInMillis(System.currentTimeMillis())
    val date = dateFormat.format(cla.getTime)
    val hour = cla.get(Calendar.HOUR_OF_DAY).toString
    val min = cla.get(Calendar.MINUTE).toString
    if(withTime & showproject){
      //add time in the log
      logger.write(s"================${project}=================\n")
      logger.write(s"[${date} ${hour}:${min}]  ${content}]\n")
    }else if(withTime & !showproject){
      logger.write(s"[${date} ${hour}:${min}]  ${content}]\n")
    }
    logger.close() //close file stream
  }
}
