package org.twelvehart.unstructured4s.examples

import cats.implicits.*
import org.twelvehart.unstructured4s.*
import org.twelvehart.unstructured4s.model.*
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.client3.*
import sttp.client3.asynchttpclient.zio.*
import sttp.client3.logging.slf4j.Slf4jLoggingBackend
import zio.logging.backend.SLF4J
import zio.*
import zio.interop.catz.*

object ZIOApp extends ZIOAppDefault:
  private val live: ZLayer[Any, Throwable, SttpBackend[Task, ZioStreams with WebSockets]] =
    ZLayer.scoped {
      AsyncHttpClientZioBackend
        .scoped()
        .map(backend =>
          Slf4jLoggingBackend(
            backend,
            logRequestHeaders = true,
            logRequestBody = true,
            sensitiveHeaders = Set("unstructured-api-key")
          )
        )
    }

  private def program(runMode: RunMode): ZIO[SttpClient, Throwable, Unit] =
    ZIO.scoped {
      for
        apiKey            <- ZIO.fromEither(apiKeyEnv)
        file              <- ZIO.fromEither(runMode.file)
        backend           <- ZIO.service[SttpClient]
        unstructuredClient = Unstructured4s.make(backend, ApiKey(apiKey))
        response          <- unstructuredClient.partition(file)
        result             = response.result.bimap(_.getMessage, _.mkString("\n")).merge
        _                 <- Console.printLine(result)
      yield ()
    }

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  override def run: ZIO[ZIOAppArgs & Scope, Any, ExitCode] =
    for {
      args <- ZIO.service[ZIOAppArgs].map(_.getArgs)
      _    <- program(handleArgs(args.toList)).provide(live)
    } yield ExitCode.success
