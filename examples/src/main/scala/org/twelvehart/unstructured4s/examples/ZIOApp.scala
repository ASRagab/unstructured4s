package org.twelvehart.unstructured4s.examples

import org.twelvehart.unstructured4s.client.*
import org.twelvehart.unstructured4s.client.model.*
import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.client3.*
import sttp.client3.asynchttpclient.zio.*
import zio.*
import zio.interop.catz.*

import java.io.File
import scala.util.Try

object ZIOApp extends ZIOAppDefault:
  val live: ZLayer[Any, Throwable, SttpBackend[Task, ZioStreams & WebSockets]] =
    AsyncHttpClientZioBackend.layer()

  val pdfEither: Either[Throwable, UnstructuredFile] = {
    val currentDir = new File(".").getCanonicalPath
    Try(new File(currentDir, "data/sample.pdf")).map(UnstructuredFile(_)).toEither
  }

  def program: ZIO[SttpClient, Throwable, Unit] =
    ZIO.scoped {
      for
        apiKey             <- ZIO.fromOption(sys.env.get("UNSTRUCTURED_API_KEY")).orElseFail(new Exception("No API key found"))
        file               <- ZIO.fromEither(pdfEither)
        backend            <- ZIO.service[SttpClient]
        unstructuredClient <- Unstructured4s.make(backend, ApiKey(apiKey))
        response           <- unstructuredClient.single(file)
        result             <- ZIO.fromEither(response.result)
        _                  <- Console.printLine(result.mkString("\n"))
      yield ()
    }

  override def run: ZIO[ZIOAppArgs & Scope, Any, Any] =
    program.provide(live)
