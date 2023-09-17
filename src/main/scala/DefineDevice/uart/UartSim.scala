package DefineDevice.uart

import DefineSim.SpinalSim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._
import java.net._
import java.io._
  /*
  wrapper the uart sim as a network pro and receive data on the other host
  */

object UartTCPDecoder {
  def apply(txd: Bool, baudPeriod: Long, start: Boolean = false, port: Int = 7845) = fork {
    val host = "localhost"
    val port = 7845
    val socket = new Socket(host, port)
    val out = new PrintWriter(socket.getOutputStream, true)
    if (start) {
      println("======================= Uart TCP Decoder =======================")
    }
    sleep(1) //Wait boot signals propagation
    waitUntil(txd.toBoolean == true)
    println(s"Uart Server started and listening on port $port")
    while (true) {
      waitUntil(txd.toBoolean == false)
      sleep(baudPeriod / 2)
      if (txd.toBoolean != false) {
        println("UART FRAME ERROR")
      }
      else {
        sleep(baudPeriod)

        var buffer = 0
        (0 to 7).foreach { bitId =>
          if (txd.toBoolean)
            buffer |= 1 << bitId
          sleep(baudPeriod)
        }

        if (txd.toBoolean != true) println("UART FRAME ERROR") else if (buffer.toChar != '\r') print(buffer.toChar)
      }
    }
    out.close()
    socket.close()
    println("Uart Server Stop!")
  }
}


object UartTCPEncoder {
  def apply(uartrx: Bool, baudPeriod: Long, start: Boolean = false) = fork {
    if (start) {
      println("======================= Uart Encoder =======================")
    }
    uartrx #= true
    while (true) {
      if (System.in.available() != 0) {
        val buffer = System.in.read()
        uartrx #= false

        sleep(baudPeriod)

        (0 to 7).foreach { bitId =>
          uartrx #= ((buffer >> bitId) & 1) != 0
          sleep(baudPeriod)
        }

        uartrx #= true
        sleep(baudPeriod)
      } else {
        sleep(baudPeriod * 1000)
      }
    }
  }
}

