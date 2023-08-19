package org.twelvehart.unstructured4s.client

import cats.effect.*
import sttp.client3.*
import sttp.model.*

export Model.{*, given}

class Unstructured4s[F[_], P](client: SttpBackend[F, P], apiKey: ApiKey):
  def post[E, A](
      request: RequestPayload,
      customHeaders: List[Header] = Nil
  )(using
      decoder: ResponseAs[Either[ResponseException[String, E], A], Any]
  ): F[Response[Either[ResponseException[String, E], A]]] =
    val headers =
      if customHeaders.nonEmpty then customHeaders
      else Unstructured4s.apiKeyHeader(apiKey) :: Unstructured4s.defaultHeaders

    client.send(
      basicRequest
        .headers(headers*)
        .post(Unstructured4s.Endpoint)
        .multipartBody(request.toMultipartSequence)
        .response(decoder)
    )

object Unstructured4s:
  def simple(apiKey: ApiKey): Unstructured4s[Identity, Any] =
    Unstructured4s(HttpClientSyncBackend(), apiKey)

  def synchronous(client: SttpBackend[Identity, Any], apiKey: ApiKey): Identity[Unstructured4s[Identity, Any]] =
    Unstructured4s(client, apiKey)

  def make[F[_]: Async, P](client: SttpBackend[F, P], apiKey: ApiKey): F[Unstructured4s[F, P]] =
    summon[Async[F]].delay(Unstructured4s(client, apiKey))

  private def apiKeyHeader(apiKey: ApiKey): Header = Header("unstructured-api-key", apiKey)

  val defaultHeaders: List[Header] = List(
    Header("Content-Type", "multipart/form-data"),
    Header("Accept", "application/json")
  )

  val Endpoint = uri"https://api.unstructured.io/general/v0/general"
end Unstructured4s
