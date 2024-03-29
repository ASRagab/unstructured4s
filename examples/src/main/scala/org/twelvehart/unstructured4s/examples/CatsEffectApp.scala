package org.twelvehart.unstructured4s.examples

import cats.effect.*
import cats.implicits.*
import org.twelvehart.unstructured4s.*
import org.twelvehart.unstructured4s.model.*
import sttp.capabilities
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3.SttpBackend
import sttp.client3.httpclient.fs2.HttpClientFs2Backend
import sttp.client3.logging.slf4j.Slf4jLoggingBackend

object CatsEffectApp extends IOApp:
  private val backendResource: Resource[IO, SttpBackend[IO, Fs2Streams[IO] & capabilities.WebSockets]] =
    HttpClientFs2Backend
      .resource[IO]()
      .map(backend =>
        Slf4jLoggingBackend(
          backend,
          logRequestHeaders = true,
          logRequestBody = true,
          sensitiveHeaders = Set("unstructured-api-key")
        )
      )

  override def run(args: List[String]): IO[ExitCode] =
    backendResource.use { backend =>
      for
        apiKey   <- IO.fromEither(apiKeyEnv)
        client    = Unstructured4s.make(backend, ApiKey(apiKey))
        file     <- IO.fromEither(handleArgs(args).file)
        response <- client.partition(file, HiResRequestFields())
        done     <- IO.println(response.result.bimap(_.getMessage, _.mkString("\n")).merge).as(ExitCode.Success)
      yield done
    }
