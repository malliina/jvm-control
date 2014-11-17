package com.mle.jvm

/**
 *
 * @author Michael
 */
object Messages {

  sealed trait Message

  case object StopRunning extends Message

  case object Hello extends Message

}
