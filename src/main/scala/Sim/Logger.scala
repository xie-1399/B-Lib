package lib.Sim

import java.io._
import java.text._
import java.util._

//create logfile in the Accelerator project

object Logger{
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
