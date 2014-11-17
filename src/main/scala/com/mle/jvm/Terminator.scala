package com.mle.jvm

import akka.actor._

/**
 * @param ref when `ref` is terminated, this actor stops its actor system
 * @see akka-sample-main-scala/#code/src/main/scala/sample/hello/Main2.scala
 */
class Terminator(ref: ActorRef) extends Actor with ActorLogging {
  context watch ref

  def receive = {
    case Terminated(_) =>
      log.info(s"${ref.path} has terminated, shutting down system")
      context.system.shutdown()
  }
}

object Terminator {
  def props(ref: ActorRef) = Props(new Terminator(ref))
}