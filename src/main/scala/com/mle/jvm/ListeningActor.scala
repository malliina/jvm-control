package com.mle.jvm

import akka.actor.{Actor, Props}
import com.mle.jvm.Messages.StopRunning

/**
 * @author Michael
 */
class ListeningActor(onStop: () => Unit) extends Actor {
  override def receive: Receive = {
    case StopRunning =>
      onStop()
      context.system.shutdown()
  }
}

object ListeningActor {
  /**
   *
   * @param onStop code to run when the created actor receives a [[StopRunning]] message
   * @return actor props
   * @see http://doc.akka.io/docs/akka/snapshot/scala/actors.html
   */
  def props(onStop: () => Unit) = Props(new ListeningActor(onStop))
}
