package org.twelvehart.unstructured4s.examples

import cats.effect.*
import org.twelvehart.unstructured4s.client.*
import org.twelvehart.unstructured4s.client.model.*
import org.twelvehart.unstructured4s.client.model.UnstructuredRequestFields.HiResRequestFields
import sttp.capabilities
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3.httpclient.fs2.HttpClientFs2Backend

import java.io.File
import scala.util.Try

object CatsEffectApp extends IOApp.Simple {
  private val sttpResource = HttpClientFs2Backend.resource[IO]()
  private val apiKeyEnv    =
    IO.fromOption(sys.env.get("UNSTRUCTURED_API_KEY"))(new Exception("UNSTRUCTURED_API_KEY not set"))

  private val unstructured4sResource: Resource[IO, IO[Unstructured4s[IO, Fs2Streams[IO] & capabilities.WebSockets]]] =
    sttpResource.map(backend => apiKeyEnv.flatMap(apiKey => Unstructured4s.make(backend, ApiKey(apiKey))))

  val pdfEither: Either[Throwable, UnstructuredFile] = {
    val currentDir = new File(".").getCanonicalPath
    Try(new File(currentDir, "data/sample.pdf")).map(UnstructuredFile(_)).toEither
  }

  private val fileIO: IO[UnstructuredFile] = IO.fromEither(pdfEither).map(file => UnstructuredFile(file))

  override def run: IO[Unit] =
    unstructured4sResource.use { unstructured4s =>
      for
        client   <- unstructured4s
        file     <- fileIO
        response <- client.single(file, HiResRequestFields())
        _        <- IO.fromEither(response.result).map(result => println(result.mkString("\n")))
      yield ()
    }

}
