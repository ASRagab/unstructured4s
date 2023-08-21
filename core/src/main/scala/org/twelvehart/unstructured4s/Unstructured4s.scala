package org.twelvehart.unstructured4s

import cats.*
import cats.implicits.*
import org.twelvehart.unstructured4s.model.{*, given}
import sttp.client3.*
import sttp.client3.circe.*
import sttp.model.*

class Unstructured4s[F[_]: Functor, P](backend: SttpBackend[F, P], apiKey: ApiKey)
    extends Unstructured4sAlg[F, PartitionResponse]:
  def partition(
      file: UnstructuredFile,
      request: Unstructured4sRequestFields = GeneralRequestFields(),
      customHeaders: List[Header] = Nil
  ): F[Unstructured4sResponse[PartitionResponse]] =
    backend
      .send(
        basicRequest
          .headers(makeHeaders(customHeaders)*)
          .post(Unstructured4s.Endpoint)
          .multipartBody(file.toMultipart :: request.toMultipartSequence)
          .response(asJson[List[PartitionResponse]])
      )
      .map(handleResponse)

  def partitionMultiple(
      files: Seq[UnstructuredFile],
      request: Unstructured4sRequestFields = GeneralRequestFields(),
      customHeaders: List[Header] = Nil
  ): F[Unstructured4sResponse[List[PartitionResponse]]] =
    val multipartBody = files.map(_.toMultipart) ++ request.toMultipartSequence

    backend
      .send(
        basicRequest
          .headers(makeHeaders(customHeaders)*)
          .post(Unstructured4s.Endpoint)
          .multipartBody(multipartBody)
          .response(asJson[List[List[PartitionResponse]]])
      )
      .map(handleResponse)

  private def handleResponse[A](
      response: Response[Either[ResponseException[String, io.circe.Error], List[A]]]
  ): Unstructured4sResponse[A] =
    response.body.fold(
      error => Unstructured4sResponse(Left(error)),
      results => Unstructured4sResponse(results.asRight[Unstructured4sError])
    )

  private def makeHeaders(custom: List[Header]): List[Header] =
    custom match
      case Nil => Unstructured4s.apiKeyHeader(apiKey) :: Unstructured4s.defaultHeaders
      case _   => custom
end Unstructured4s

object Unstructured4s:
  def make[F[_]: Functor, P](backend: SttpBackend[F, P], apiKey: ApiKey): Unstructured4s[F, P] =
    Unstructured4s(backend, apiKey)

  def apiKeyHeader(apiKey: ApiKey): Header = Header("unstructured-api-key", apiKey)

  val defaultHeaders: List[Header] = List(
    Header("Content-Type", "multipart/form-data"),
    Header("Accept", "application/json")
  )

  val Endpoint = uri"https://api.unstructured.io/general/v0/general"
end Unstructured4s
