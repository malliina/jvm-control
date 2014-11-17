package com.mle.jvm

import akka.actor._
import com.mle.jvm.Messages.StopRunning

import scala.concurrent.duration.DurationInt

/**
 * Actor that identifies an actor at `path` and sends it a [[StopRunning]] message.
 *
 * @author Michael
 */
class LookupActor(path: String) extends Actor with ActorLogging {

  sendIdentifyRequest()

  def sendIdentifyRequest(): Unit = {
    context.actorSelection(path) ! Identify(path)
    import context.dispatcher
    context.system.scheduler.scheduleOnce(10.seconds, self, ReceiveTimeout)
  }

  def receive = stopping

  def stopping: Actor.Receive = {
    case ActorIdentity(`path`, Some(actor)) =>
      log.info(s"Found system at: $path, asking it to stop...")
      // watching an actor will cause a Terminated(actor) message to be received when it stops
      context.watch(actor)
      actor ! StopRunning
    case ActorIdentity(`path`, None) =>
      log.info(s"Remote system not available: $path")
      context.stop(self)
    case ReceiveTimeout =>
      log.info(s"Timed out while identifying: $path")
      context.stop(self)
    case Terminated(_) =>
      log.info(s"Terminated: $path")
      context.stop(self)
    case _ =>
      log.info("Not ready yet")
  }
}

object LookupActor {
  def props(path: String) = Props(new LookupActor(path))
}