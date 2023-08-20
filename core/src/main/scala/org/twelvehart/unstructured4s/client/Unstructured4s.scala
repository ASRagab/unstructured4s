package org.twelvehart.unstructured4s.client

import cats.*
import cats.effect.*
import cats.implicits.*
import cats.syntax.either.given
import org.twelvehart.unstructured4s.client.model.UnstructuredRequestFields.GeneralRequestFields
import org.twelvehart.unstructured4s.client.model.{*, given}
import sttp.client3.*
import sttp.client3.circe.*
import sttp.model.*

import scala.language.postfixOps

class Unstructured4s[F[_]: Functor, P](backend: SttpBackend[F, P], apiKey: ApiKey):
  def single(
      file: UnstructuredFile,
      request: UnstructuredRequestFields = GeneralRequestFields(),
      customHeaders: List[Header] = Nil
  ): F[Unstructured4sResponse[ResponsePayload]] =
    val headers =
      if customHeaders.nonEmpty then customHeaders
      else Unstructured4s.apiKeyHeader(apiKey) :: Unstructured4s.defaultHeaders

    backend
      .send(
        basicRequest
          .headers(headers*)
          .post(Unstructured4s.Endpoint)
          .multipartBody(file.toMultipart :: request.toMultipartSequence)
          .response(asJson[List[ResponsePayload]])
      )
      .map(
        _.body.fold(
          error => Unstructured4sResponse(Left(error)),
          results => Unstructured4sResponse(results.asRight[Unstructured4sError])
        )
      )

  def multiple(
      files: Seq[UnstructuredFile],
      request: UnstructuredRequestFields = GeneralRequestFields(),
      customHeaders: List[Header] = Nil
  ): F[Unstructured4sResponse[List[ResponsePayload]]] =
    val headers =
      if customHeaders.nonEmpty then customHeaders
      else Unstructured4s.apiKeyHeader(apiKey) :: Unstructured4s.defaultHeaders

    backend
      .send(
        basicRequest
          .headers(headers*)
          .post(Unstructured4s.Endpoint)
          .multipartBody(files.map(_.toMultipart) ++ request.toMultipartSequence)
          .response(asJson[List[List[ResponsePayload]]])
      )
      .map(
        _.body.fold(
          error => Unstructured4sResponse(Left(error)),
          results => Unstructured4sResponse(results.asRight[Unstructured4sError])
        )
      )
end Unstructured4s

object Unstructured4s:
  def simple(apiKey: ApiKey): Unstructured4s[Identity, Any] =
    Unstructured4s(HttpClientSyncBackend(), apiKey)

  def make[F[_]: Async, P](client: SttpBackend[F, P], apiKey: ApiKey): F[Unstructured4s[F, P]] =
    summon[Async[F]].delay(Unstructured4s(client, apiKey))

  private def apiKeyHeader(apiKey: ApiKey): Header = Header("unstructured-api-key", apiKey)

  val defaultHeaders: List[Header] = List(
    Header("Content-Type", "multipart/form-data"),
    Header("Accept", "application/json")
  )
  val Endpoint                     = uri"https://api.unstructured.io/general/v0/general"

  private[unstructured4s] def test(
      client: SttpBackend[Identity, Any],
      apiKey: ApiKey
  ): Identity[Unstructured4s[Identity, Any]] =
    Unstructured4s(client, apiKey)
end Unstructured4s
