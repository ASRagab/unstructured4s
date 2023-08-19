import sbt.*

object V {
  lazy val zio   = "2.0.15"
  lazy val sttp  = "3.9.0"
  lazy val circe = "0.14.5"
}

object Dependencies {
  lazy val cats              = "org.typelevel" %% "cats-core"                     % "2.9.0"
  lazy val catsEffect        = "org.typelevel" %% "cats-effect"                   % "3.5.1"
  lazy val catsEffectTesting = "org.typelevel" %% "cats-effect-testing-scalatest" % "1.5.0" % Test

  lazy val scalaTest  = "org.scalatest"  %% "scalatest"  % "3.2.16" % Test
  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.17.0" % Test

  lazy val sttpFs2     = "com.softwaremill.sttp.client3" %% "fs2"                           % V.sttp
  lazy val sttpSlf4j   = "com.softwaremill.sttp.client3" %% "slf4j-backend"                 % V.sttp
  lazy val sttpCirce   = "com.softwaremill.sttp.client3" %% "circe"                         % V.sttp
  lazy val sttpZio     = "com.softwaremill.sttp.client3" %% "async-http-client-backend-zio" % V.sttp
  lazy val sttpZioJson = "com.softwaremill.sttp.client3" %% "zio-json"                      % V.sttp

  lazy val circe        = "io.circe" %% "circe-core"    % V.circe
  lazy val circeGeneric = "io.circe" %% "circe-generic" % V.circe
  lazy val circeParser  = "io.circe" %% "circe-parser"  % V.circe

  lazy val zioTest    = "dev.zio" %% "zio-test"         % V.zio
  lazy val zio        = "dev.zio" %% "zio"              % V.zio
  lazy val zioTestSbt = "dev.zio" %% "zio-test-sbt"     % V.zio
  lazy val zioCats    = "dev.zio" %% "zio-interop-cats" % "23.0.0.5"
  lazy val zioJson    = "dev.zio" %% "zio-json"         % "0.5.0"

  lazy val core: Seq[ModuleID] = Seq(
    cats,
    catsEffect,
    sttpFs2,
    sttpCirce,
    sttpSlf4j,
    circe,
    circeGeneric,
    circeParser
  )

  lazy val examples: Seq[ModuleID] = Seq(
    zio,
    zioCats,
    cats,
    catsEffect,
    sttpSlf4j,
    sttpZio,
    sttpFs2,
    zioJson,
    sttpZioJson
  )
}
