package org.twelvehart.unstructured4s.examples

import cats.effect.*
import org.twelvehart.unstructured4s.client.*
import sttp.capabilities
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3.httpclient.fs2.HttpClientFs2Backend
import io.circe.*
import sttp.client3.{DeserializationException, ResponseAs}
import sttp.client3.circe.*

import java.io.File

object CatsEffectApp extends IOApp.Simple {
  private val sttpResource = HttpClientFs2Backend.resource[IO]()

  private val unstructured4sResource: Resource[IO, IO[Unstructured4s[IO, Fs2Streams[IO] & capabilities.WebSockets]]] =
    sttpResource.map(backend => Unstructured4s.make(backend, ApiKey("<your-api-key>")))

  private val fileIO: IO[UnstructuredFile] =
    IO
      .delay(new File(getClass.getClassLoader.getResource("sample.pdf").getFile))
      .map(file => UnstructuredFile(file))

  given responseAs: ResponseAs[Either[DeserializationException[Error], Json], Any] =
    asJsonAlways[Json]

  override def run: IO[Unit] =
    unstructured4sResource.use { unstructured4s =>
      for
        client   <- unstructured4s
        file     <- fileIO
        request   = HiResPayload(Seq(file))
        response <- client.post[Error, Json](request)
        _        <- IO.fromEither(response.body).map(json => println(json.spaces2))
      yield ()
    }

}
