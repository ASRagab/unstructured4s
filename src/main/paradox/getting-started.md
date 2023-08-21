# Getting Started

## Api Key

You will need to obtain an [apikey] from [Unstructured.io]

[apikey]: https://unstructured.io/#get-api-key
[Unstructured.io]: https://unstructured.io

By default, an apikey is needed to make the client; however, individual requests can
also be provided a separate apikey, which will override the default, when passed a header,
see further below.

## Instantiating the Client

In addition to the `unstructured4s-core` module, you will need to provide an `sttp` backend, you can use any of the backends provided by [sttp] as long as you can provide a client with the effect capability `F[_]` that has a `Functor` instance (this will typical require some kind of interoperability module with the `cats` typeclass hierarchy)

[sttp]: https://sttp.softwaremill.com/en/latest/backends/summary.html


<br/>
First obtain the necessary dependencies for `sttp`, here are the ones for `ZIO`:


@@dependency[sbt,Maven,Gradle] {
    group="com.softwaremill.sttp.client3" artifact="async-http-client-backend-zio" version="latest"
    group2="dev.zio" artifact2="zio-interop-cats" version2="latest"
}

<br/>
Here is the one for the `fs2` backend:


@@dependency[sbt,Maven,Gradle] {
    group="com.softwaremill.sttp.client3" 
    artifact="async-http-client-backend-fs2"
    version="latest"
}


<br/>
To instantiate the client, first include these imports:

```scala
import org.twelvehart.unstructured4s.*
import org.twelvehart.unstructured4s.model.*
import sttp.client3.*
```

Next, declare the backend and retrieve the apikey from the environment:

```scala
object BasicApp extends App {
  private val backend = HttpClientSyncBackend()
  private val client = Unstructured4s.make(backend, ApiKey(apiKey))
}
```

<br/>
For `ZIO` you will likely be making a `ZLayer`:

```scala
import zio.*
import zio.interop.catz.*
import org.twelvehart.unstructured4s.*
import org.twelvehart.unstructured4s.model.*
import sttp.client3.asynchttpclient.zio.*
import sttp.client3.*

object ZIOApp extends ZIOAppDefault {
  private val live = AsyncHttpClientZioBackend.layer()

  def program: ZIO[SttpClient, Throwable, Unit] =
    for {
      backend <- ZIO.service[SttpClient]
      _       <- Unstructured4s.make(backend, ApiKey(apiKey))
    } yield ()
}
```

<br/>
For `fs2` you will likely be making a `cats.effect.Resource`:

```scala
import cats.effect.*
import org.twelvehart.unstructured4s.*
import org.twelvehart.unstructured4s.model.*
import sttp.capabilities
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3.SttpBackend
import sttp.client3.httpclient.fs2.HttpClientFs2Backend
import sttp.client3.logging.slf4j.Slf4jLoggingBackend

object CatsEffectApp extends IOApp.Simple {
  private val backendResource: Resource[IO, SttpBackend[IO, Fs2Streams[IO] & capabilities.WebSockets]] =
    HttpClientFs2Backend
      .resource[IO]()
      .map(backend =>
        // You can even use logged backends as an example
        Slf4jLoggingBackend(
          backend,
          logRequestHeaders = false,
          logRequestBody = true
        )
      )

  override def run: IO[Unit] =
    backendResource.use { backend =>
      for {
        client <- Unstructured4s.make(backend, ApiKey(apiKey))
      } yield ()
    }
```
