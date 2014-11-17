# jvm-control #

Gracefully start/stop JVM instances. An alternative to fooling around with kill commands and pidfiles.

## Installation ##

```"com.github.malliina" %% "jvm-control" % "0.1.0"```

## Usage ##

During app startup, please call ```com.mle.jvm.Control.startSystem(onStop = ???)```, passing some code to run when the
system is eventually stopped.

To stop the app, run main class ```com.mle.jvm.Stop``` or call (from another JVM) ```com.mle.jvm.Control.stopSystem()```.

## How it works ##

```Control.startSystem()``` starts a listening actor that accepts messages from other local JVMs. Running ```com.mle.jvm.Stop``` starts a JVM and creates an actor that looks up the listening actor and sends it a shutdown message. Both parties will then stop.

SSL with client authentication and proper file permissions should prevent any local JVM from potentially stopping any another local JVM.