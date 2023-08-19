package org.twelvehart.unstructured4s.examples

import org.twelvehart.unstructured4s.client.*
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.client3.*
import sttp.client3.ziojson.*
import sttp.client3.asynchttpclient.zio.*
import zio.interop.catz.*
import zio.*
import zio.json.*
import zio.json.ast.Json

import java.io.File

object ZIOApp extends ZIOAppDefault:
  val live: ZLayer[Any, Throwable, SttpBackend[Task, ZioStreams with WebSockets]] =
    AsyncHttpClientZioBackend.layer()

  val file: UnstructuredFile =
    UnstructuredFile(new File(getClass.getClassLoader.getResource("sample.pdf").getFile))

  val request: HiResPayload = HiResPayload(Seq(file))

  given decoder: ResponseAs[Either[ResponseException[String, String], Json], Any] =
    asJsonAlways[Json]

  def program: ZIO[SttpClient, Throwable, Unit] =
    ZIO.scoped {
      for
        backend            <- ZIO.service[SttpClient]
        unstructuredClient <- Unstructured4s.make(backend, ApiKey("<your-api-key>"))
        response           <- unstructuredClient.post(request)
        body               <- ZIO.fromEither(response.body)
        _                  <- Console.printLine(body.toJsonPretty)
      yield ()
    }

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    program.provide(live)
