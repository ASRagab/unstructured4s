Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "3.3.0"

addCommandAlias("fmt", "scalafmtAll")

lazy val commonOptions =
  Seq("-Xfatal-warnings", "-source:future", "-Ykind-projector:underscores", "-Xmax-inlines", "100")

val commonSettings = Seq(
  scalacOptions ++= commonOptions
)

lazy val unstructured4s = (project in file("."))
  .settings(
    commonSettings,
    publish / skip := true
  )
  .aggregate(core, examples)

lazy val core = (project in file("core"))
  .settings(
    commonSettings,
    name := "unstructured4s-core",
    libraryDependencies ++= Dependencies.core
  )

lazy val examples = (project in file("examples"))
  .dependsOn(core)
  .settings(
    commonSettings,
    name           := "unstructured4s-examples",
    publish / skip := true
  )
