package lib.Sim

import spinal.core._
import spinal.lib._
import spinal.lib.sim._

object StramFlowSim{
  //wrapper the Stream driver and Stream monitor using function
    def StreamSimulation[T <: Data](s:Stream[T],useDriver:Boolean = false, setValue:(T) => Boolean = null,
                                    useMonitor:Boolean = false,monitor:(T) => Unit = null,clockDomain: ClockDomain) = {
      //init Stream data
      if (useDriver) {
        StreamDriver(s, clockDomain = clockDomain) {
          payload =>
            setValue(payload)
        }
      }
      //Monitor Stream data
      if (useMonitor) {
        StreamMonitor(s, clockDomain) {
          payload =>
            monitor(payload)
        }
      }
      "Stream Simulation"
    }

    def ScoreBoardSimulation[T]() = {
      //get a new board (pushDut and pushRef)
      val scoreboardInOrder = ScoreboardInOrder[T]()
      scoreboardInOrder
    }

}
