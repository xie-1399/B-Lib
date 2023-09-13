package DefinePipeline.build

import spinal.core._
  /*
  this file shows about how the vexriscv pipeline build
  and insert the pipeline with the plugin in it
 */



class Stageable[T <: Data](_dataType: => T) extends HardType[T](_dataType) with Nameable {
   /* for HardType will no need of clone of T*/
  def dataType() = apply()
  setWeakName(this.getClass.getSimpleName.replace("$",""))
}


class VexPipeline {

}

object VexPipeline extends App{
  println(new Stageable(Bool()).getName())
}
