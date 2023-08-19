Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

ThisBuild / crossScalaVersions := Seq("2.13.10", "3.3.0")

ThisBuild / scalacOptions ++= {
  val commonOptions =
    Seq(
      "-Xmax-inlines",
      "100",
      "-Xfatal-warnings"
    )
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _))       => Seq("-Ykind-projector:underscores") ++ commonOptions
    case Some((2, 12 | 13)) =>
      Seq("-Xsource:3", "-P:kind-projector:underscore-placeholders") ++ commonOptions
    case _                  => Nil
  }
}

lazy val unstructured4s = (project in file("."))
  .settings(
    crossScalaVersions := Nil,
    publish / skip     := true
  )
  .aggregate(core, examples)

lazy val core = (project in file("core"))
  .settings(
    name := "unstructured4s-core",
    libraryDependencies ++= Dependencies.core
  )

lazy val examples = (project in file("examples"))
  .dependsOn(core)
  .settings(
    name           := "unstructured4s-examples",
    libraryDependencies ++= Dependencies.examples,
    publish / skip := true
  )
