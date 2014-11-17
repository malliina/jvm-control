package com.mle.jvm

/**
 * @author Michael
 */
object Start {
  val START = "start"
  val STOP = "stop"

  import com.mle.jvm.Control.{startSystem, stopSystem}

  def main(args: Array[String]): Unit = {
    val cmd = args.headOption.filter(_ == STOP) getOrElse START
    cmd match {
      case START => startSystem(onStop = stopRunning)
      case STOP => stopSystem()
    }
  }
  def stopRunning(): Unit = {
//    println("bye!")
  }
}
