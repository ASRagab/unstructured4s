package org.twelvehart.unstructured4s.client.model

import io.circe.*
import sttp.client3.{DeserializationException, HttpError, ResponseException}

enum Unstructured4sError(val body: String, val error: String) extends Exception(error):
  case JsonParseError(override val body: String, override val error: String) extends Unstructured4sError(body, error)
  case UnhandledResponseError(override val body: String, val statusCode: String)
      extends Unstructured4sError(body, statusCode)

given Conversion[ResponseException[String, io.circe.Error], Unstructured4sError] with
  def apply(responseException: ResponseException[String, io.circe.Error]): Unstructured4sError =
    responseException match
      case DeserializationException(body, error) => Unstructured4sError.JsonParseError(body, error.getMessage)
      case HttpError(body, code)                 => Unstructured4sError.UnhandledResponseError(body, code.toString)

case class Unstructured4sResponse[A](result: Either[Unstructured4sError, List[A]])
