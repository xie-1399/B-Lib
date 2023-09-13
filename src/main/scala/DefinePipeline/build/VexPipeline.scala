package DefinePipeline.build

import DefineSim.SpinalSim._
import spinal.core._
import spinal.lib._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
  /*
  this file shows about how the vexriscv pipeline build
  and insert the pipeline with the plugin in it
 */

class Stageable[T <: Data](_dataType: => T) extends HardType[T](_dataType) with Nameable {
   /* for HardType will no need of clone of T*/
  def dataType() = apply()
  setWeakName(this.getClass.getSimpleName.replace("$",""))
}

class Stage() extends Area{
  /* the LinkedHashMap will keep the key and value insert order*/
  val inputs = mutable.LinkedHashMap[Stageable[Data],Data]()
  val inputsDefault = mutable.LinkedHashMap[Stageable[Data],Data]()
  val outputs = mutable.LinkedHashMap[Stageable[Data],Data]()
  val outputsDefault = mutable.LinkedHashMap[Stageable[Data],Data]()
  val inserts = mutable.LinkedHashMap[Stageable[Data],Data]()
  val dontSample  = mutable.LinkedHashMap[Stageable[_], ArrayBuffer[Bool]]()

  def outsideCondScope[T](that: => T): T = {
    val body = Component.current.dslBody /*  Get the head of the current component symbols tree */
    val ctx = body.push()
    val swapContext = body.swap()
    val ret = that
    ctx.restore()
    swapContext.appendBack()
    ret
  }

  def input[T <: Data](key:Stageable[T]):T = {
    inputs.getOrElseUpdate(key.asInstanceOf[Stageable[Data]],outsideCondScope{
      val input,inputDefault = key.dataType()
      inputsDefault(key.asInstanceOf[Stageable[Data]]) = inputDefault
      input := inputDefault
      input
    }).asInstanceOf[T]
  }


  def output[T <: Data](key: Stageable[T]): T = {
    outputs.getOrElseUpdate(key.asInstanceOf[Stageable[Data]], outsideCondScope {
      val output, outputDefault = key()
      outputsDefault(key.asInstanceOf[Stageable[Data]]) = outputDefault
      output := outputDefault
      output
    }).asInstanceOf[T]
  }

  def insert[T <: Data](key: Stageable[T]): T = {
    inserts.getOrElseUpdate(key.asInstanceOf[Stageable[Data]],outsideCondScope{
      key()
    }).asInstanceOf[T]
  }

  //the stage control signal here
  val arbitration = new Area {
    val haltItself = False //user settable, stuck the instruction, should only be set by the instruction itself
    val haltByOther = False //When settable, stuck the instruction, should only be set by something else than the stucked instruction
    val removeIt = False //When settable, unschedule the instruction as if it was never executed (no side effect)
    val flushIt = False //When settable, unschedule the current instruction
    val flushNext = False //When settable, unschedule instruction above in the pipeline
    val isValid = Bool() //Inform if a instruction is in the current stage
    val isStuck = Bool() //Inform if the instruction is stuck (haltItself || haltByOther)
    val isStuckByOthers = Bool() //Inform if the instruction is stuck by sombody else
    def isRemoved = removeIt //Inform if the instruction is going to be unschedule the current cycle
    val isFlushed = Bool() //Inform if the instruction is flushed (flushAll set in the current or subsequents stages)
    val isMoving = Bool() //Inform if the instruction is going somewere else (next stage or unscheduled)
    val isFiring = Bool() //Inform if the current instruction will go to the next stage the next cycle (isValid && !isStuck && !removeIt)
  }

  def dontSampleStageable(s: Stageable[_], cond: Bool): Unit = {
    dontSample.getOrElseUpdate(s, ArrayBuffer[Bool]()) += cond
  }
}

trait Pipeline {
  type T <: Pipeline
  var stages = ArrayBuffer[Stage]()
  var unremovableStages = mutable.Set[Stage]()

  def stageBefore(stage : Stage) = stages(indexOf(stage)-1)

  def indexOf(stage : Stage) = stages.indexOf(stage)


  def build(): Unit ={
    //Interconnect stages
    class KeyInfo{
      var insertStageId = Int.MaxValue
      var lastInputStageId = Int.MinValue
      var lastOutputStageId = Int.MinValue

      def addInputStageIndex(stageId : Int): Unit = {
        require(stageId >= insertStageId)
        lastInputStageId = Math.max(lastInputStageId,stageId)
        lastOutputStageId = Math.max(lastOutputStageId,stageId-1)
      }


      def addOutputStageIndex(stageId : Int): Unit = {
        require(stageId >= insertStageId)
        lastInputStageId = Math.max(lastInputStageId,stageId)
        lastOutputStageId = Math.max(lastOutputStageId,stageId)
      }

      def setInsertStageId(stageId : Int) = insertStageId = stageId
    }

    val inputOutputKeys = mutable.LinkedHashMap[Stageable[Data],KeyInfo]()
    val insertedStageable = mutable.Set[Stageable[Data]]()
    for(stageIndex <- 0 until stages.length; stage = stages(stageIndex)){
      stage.inserts.keysIterator.foreach(signal => inputOutputKeys.getOrElseUpdate(signal,new KeyInfo).setInsertStageId(stageIndex))
      stage.inserts.keysIterator.foreach(insertedStageable += _)
    }

    val missingInserts = mutable.Set[Stageable[Data]]()
    for(stageIndex <- 0 until stages.length; stage = stages(stageIndex)){
      stage.inputs.keysIterator.foreach(key => if(!insertedStageable.contains(key)) missingInserts += key)
      stage.outputs.keysIterator.foreach(key => if(!insertedStageable.contains(key)) missingInserts += key)
    }

    if(missingInserts.nonEmpty){
      throw new Exception("Missing inserts : " + missingInserts.map(_.getName()).mkString(", "))
    }

    for(stageIndex <- 0 until stages.length; stage = stages(stageIndex)){
      stage.inputs.keysIterator.foreach(key => inputOutputKeys.getOrElseUpdate(key,new KeyInfo).addInputStageIndex(stageIndex))
      stage.outputs.keysIterator.foreach(key => inputOutputKeys.getOrElseUpdate(key,new KeyInfo).addOutputStageIndex(stageIndex))
    }

    for((key,info) <- inputOutputKeys) {
      //Interconnect inputs -> outputs
      for (stageIndex <- info.insertStageId to info.lastOutputStageId;
           stage = stages(stageIndex)) {
        stage.output(key)
        val outputDefault = stage.outputsDefault.getOrElse(key, null)
        if (outputDefault != null) {
          outputDefault := stage.input(key)
        }
      }

      //Interconnect outputs -> inputs
      for (stageIndex <- info.insertStageId to info.lastInputStageId) {
        val stage = stages(stageIndex)
        stage.input(key)
        val inputDefault = stage.inputsDefault.getOrElse(key, null)
        if (inputDefault != null) {
          if (stageIndex == info.insertStageId) {
            inputDefault := stage.inserts(key)
          } else {
            val stageBefore = stages(stageIndex - 1)
            inputDefault := RegNextWhen(stageBefore.output(key), stage.dontSample.getOrElse(key, Nil).foldLeft(!stage.arbitration.isStuck)(_ && !_)).setName(s"${stageBefore.getName()}_to_${stage.getName()}_${key.getName()}")
          }
        }
      }
    }

    //Arbitration
    for(stageIndex <- 0 until stages.length; stage = stages(stageIndex)) {
      stage.arbitration.isFlushed := stages.drop(stageIndex+1).map(_.arbitration.flushNext).orR || stages.drop(stageIndex).map(_.arbitration.flushIt).orR
      if(!unremovableStages.contains(stage))
        stage.arbitration.removeIt setWhen stage.arbitration.isFlushed
      else
        assert(stage.arbitration.removeIt === False,"removeIt should never be asserted on this stage")

    }

    for(stageIndex <- 0 until stages.length; stage = stages(stageIndex)){
      stage.arbitration.isStuckByOthers := stage.arbitration.haltByOther || stages.takeRight(stages.length - stageIndex - 1).map(s => s.arbitration.isStuck/* && !s.arbitration.removeIt*/).foldLeft(False)(_ || _)
      stage.arbitration.isStuck := stage.arbitration.haltItself || stage.arbitration.isStuckByOthers
      stage.arbitration.isMoving := !stage.arbitration.isStuck && !stage.arbitration.removeIt
      stage.arbitration.isFiring := stage.arbitration.isValid && !stage.arbitration.isStuck && !stage.arbitration.removeIt
    }

    for(stageIndex <- 1 until stages.length){
      val stageBefore = stages(stageIndex - 1)
      val stage = stages(stageIndex)
      stage.arbitration.isValid.setAsReg() init(False)
      when(!stage.arbitration.isStuck || stage.arbitration.removeIt) {
        stage.arbitration.isValid := False
      }
      when(!stageBefore.arbitration.isStuck && !stageBefore.arbitration.removeIt) {
        stage.arbitration.isValid := stageBefore.arbitration.isValid
      }
    }
  }
  Component.current.addPrePopTask(() => build())
}

 /*
  create the pipeline with two stages and -> them can connect self
 */

class VexPipeline extends PrefixComponent with Pipeline {
  val io = new Bundle{
    val cond = in Bool()
  }

  def newStage(): Stage = {
    val s = new Stage; stages += s; s
  }

  val decode = newStage()
  val execute = newStage()
  def stagesFromExecute = stages.dropWhile(_ != execute)

  val stagebool = new Stageable(Bool()).apply()
  stagebool := io.cond
}

object VexPipeline extends App{
  val rtl = new RtlConfig().GenRTL(top = new (VexPipeline),pruned = false)
}
