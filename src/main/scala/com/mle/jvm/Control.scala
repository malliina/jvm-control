package com.mle.jvm

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

/**
 * @author Michael
 */
object Control {
  val listenerActorSystemName = "AppSystem"
  val listenerName = "listener"

  val lookupActorSystemName = "LookupSystem"
  val lookupName = "lookupActor"
  val terminatorName = "localTerminator"

  /**
   * Starts a [[ListeningActor]] that listens for shutdown messages.
   */
  def startSystem(onStop: () => Unit): Unit = {
    val config = ConfigFactory.load("listen")
    val system = ActorSystem(listenerActorSystemName, config)
    system.actorOf(ListeningActor.props(onStop), listenerName)
  }

  /**
   * Creates an actor system, then looks up a [[ListeningActor]] on another local JVM and sends it a shutdown message.
   * Once the other JVM has been terminated, the created actor system also shuts down.
   */
  def stopSystem(): Unit = {
    val listenerConf = ConfigFactory.load("listen")
    val listenerHost = listenerConf getString "akka.remote.netty.tcp.hostname"
    val listenerPort = listenerConf getString "akka.remote.netty.tcp.port"
    val transports = listenerConf getStringList "akka.remote.enabled-transports"
    val protocol = if (transports contains "akka.remote.netty.ssl") "akka.ssl.tcp" else "akka.tcp"
    val system = ActorSystem(lookupActorSystemName, ConfigFactory.load("lookup"))
    val listenerPath = s"$protocol://$listenerActorSystemName@$listenerHost:$listenerPort/user/$listenerName"
    // lookupActor will, upon creation, identify the remote actor at listenerPath and ask it to stop
    val lookupActor = system.actorOf(LookupActor.props(listenerPath), lookupName)
    // lookupActor stops when the other system has stopped, or if something goes wrong.
    // We want to shut down when any of that happens.
    system.actorOf(Terminator.props(lookupActor), terminatorName)
  }
}
