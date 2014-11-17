import com.mle.sbtutils.{SbtProjects, SbtUtils}
import sbt.Keys._
import sbt._

/**
 * A scala build file template.
 */
object AkkaBuild extends Build {

  lazy val akka = SbtProjects.mavenPublishProject("jvm-control").settings(projectSettings: _*)

  val akkaOrg = "com.typesafe.akka"
  val akkaVersion = "2.3.7"

  lazy val projectSettings = Seq(
    SbtUtils.gitUserName := "malliina",
    SbtUtils.developerName := "Michael Skogberg",
    version := "0.1.0",
    scalaVersion := "2.11.4",
    fork in Test := true,
    libraryDependencies ++= Seq(
      akkaOrg %% "akka-actor" % akkaVersion,
      akkaOrg %% "akka-remote" % akkaVersion)
  )
}