package DefineSim

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest._
import org.scalatest.events._

import java.io.{File,FileWriter,OutputStream,PrintWriter}
import java.text._
import java.util._
import spinal.core._
import spinal.core.sim._
import scala.io.Source
import scala.collection.mutable.ListBuffer
import java.nio.file.{Files, Paths}

object Logger {
  /* the print format of the log value*/
  def apply(value: UInt, hex: Boolean = false) = {
    val res = if (hex) value.toLong.toHexString else value.toLong
    println(value.name + "   is  " + res)
  }

  def apply(value: Bool, binary: Boolean) = {
    val res = if (binary) value.toBoolean.toInt else value.toBoolean
    println(value.name + "   is  " + res)
  }

  def apply(value: SInt, binary: Boolean) = {
    val res = if (binary) value.toLong.toBinaryString else value.toLong
    println(value.name + "   is  " + res)
  }

  def apply(value: Bits, signal: Boolean, bitWidth: Int): Unit = {
    val res = if (signal) {
      unsignedToSigned(value.toLong, bitWidth)
    }
    else {
      value.toLong
    }
    println(value.name + "   is  " + res)
  }

  /* a convert to the signed number */
  def unsignedToSigned(unsignedValue: Long, bitWidth: Int): Long = {
    val maxUnsignedValue = (1L << bitWidth) - 1L
    if (unsignedValue <= maxUnsignedValue / 2) {
      unsignedValue
    } else {
      unsignedValue - (maxUnsignedValue + 1)
    }
  }

  /* a very long string split with some width */
  def StringSplitShow(str: String, num: Int) = {
    for (idx <- 0 until str.length) {
      print(str(idx))
      if ((idx + 1) % num == 0) {
        print("\t")
      }
    }
    print("\n")
  }

  /* convert the bigInt value to the binary with width */
  def bigintToBinaryStringWithWidth(bigIntValue: BigInt, width: Int): String = {
    val binaryString = bigIntValue.toString(2)
    if (binaryString.length < width) {
      "0" * (width - binaryString.length) + binaryString
    } else {
      binaryString
    }
  }

  /* convert the hex string with width */
  def HexStringWithWidth(hex: String, width: Int, fill: String = "0",left:Boolean = true): String = {
    if (hex.length < width) {
      if(left){
        fill * (width - hex.length) + hex
      }else{
        hex + fill * (width - hex.length)
      }
    } else {
      hex
    }
  }

  /* from the binary get the signal value */
  def binaryComplementEncode(binaryString: String): BigInt = {
    val isNegative = binaryString(0) == '1'
    val bigInt = BigInt(binaryString, 2)
    if (isNegative) {
      /* calculate the complement */
      val complement = bigInt - (BigInt(1) << binaryString.length)
      complement
    } else {
      bigInt
    }
  }

  /* fill the string with the word */
  def fillWithWordIndex(value:String,valueWidth:Int,dataWidth:Int,word:Int,fill:String = "0") = {
    require(dataWidth % valueWidth == 0)
    require(value.length == valueWidth)
    val groups = dataWidth % valueWidth
    var realbin = ""
    for(idx <- 0 until groups){
      if(idx == word ) {
        realbin += value
      }else{
        // realbin += HexStringWithWidth(fill,valueWidth)
      }
    }
    println(realbin)
    // BigInt(realbin,2)
  }

  def CreateloggerFile(logpath: String = "./results.log", clear: Boolean = false) = {
    if (clear) {
      val file = new File(logpath)
      file.delete()
    }
    val logger = new FileWriter(logpath, true)
    logger
  }

  /* read the file into and print the format */
  def readFile(filePath:String,logIt:Boolean = false) = {
    /* read the file line by line */
    val fileSource = Source.fromFile(filePath)
    if(logIt){
     for(lines <- fileSource.getLines()){
       println(lines) /* the file will show in the format */
     }
    }
    fileSource.getLines()
  }

  /* like the python os.list dir show the file name under the dir*/
  def listDir(directoryPath: String): Seq[String] = {
    val path = Paths.get(directoryPath)
    val fileNames = Files.list(path)
      .toArray
      .map(_.toString)

    fileNames.toSeq
  }


  def logout(content: String, project: String = null, showproject: Boolean = false,
             withTime: Boolean = true, logpath: String = "./results.log", clear: Boolean = false): Unit = {
    val logger = CreateloggerFile(logpath, clear)
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val cla = Calendar.getInstance()
    cla.setTimeInMillis(System.currentTimeMillis())
    val date = dateFormat.format(cla.getTime)
    val hour = cla.get(Calendar.HOUR_OF_DAY).toString
    val min = cla.get(Calendar.MINUTE).toString
    if (withTime & showproject) {
      //add time in the log
      logger.write(s"================${project}=================\n")
      logger.write(s"[${date} ${hour}:${min}]  ${content}\n")
    } else if (withTime & !showproject) {
      logger.write(s"[${date} ${hour}:${min}]  ${content}\n")
    } else {
      logger.write(s"\t ${content}\n")
    }
    logger.close() //close file stream
  }

  def SimReport(test:AnyFunSuite,testName:Option[String],saved:Boolean = true,
                file:String = "simulation.log",clear:Boolean = false) = {
    val outputStream = new TestOutputStream()

    Console.withOut(outputStream) {
      Console.withErr(outputStream) {
        test.run(testName, args = Args(new CustomReporter)).toString
      }
    }
    val capturedOutput = outputStream.toString()
    println(capturedOutput) // Get the captured output
    if(saved){
      val logger = CreateloggerFile(file, clear)
      logger.write(capturedOutput)
      logger.write("\n" * 4)
      logger.close()
      println(s"saved it on ${file}, check it:)")
    }

  }

}

class TestOutputStream extends OutputStream {
  private val buffer = new ListBuffer[Byte]()

  override def write(b: Int): Unit = {
    buffer += b.toByte
  }
  override def toString(): String = new String(buffer.toArray)
}

class CustomReporter extends Reporter {
  override def apply(event: Event): Unit = {
    event match {
      case event: RunStarting =>
        println(s"Test run starting: ${event.testCount} tests to run.")
      case event: TestStarting =>
        println(s"===== starting: ${event.testName}=====")
      case event: TestSucceeded =>
        println(s"===== Test succeeded: ${event.testName}=====")
      case event: TestFailed =>
        println(s"Test failed: ${event.testName}")
        println(s"  - Error Message: ${event.message}")
      case _ => // Handle other event types, if necessary
    }
  }
}