package org.twelvehart.unstructured4s.model

import io.circe.*
import io.circe.generic.semiauto.*
import sttp.client3.{DeserializationException, HttpError, ResponseException}

// #partition_response
case class PartitionResponse(`type`: String, elementId: String, metadata: Metadata, text: String)
// #partition_response

object PartitionResponse:
  given Decoder[PartitionResponse] =
    Decoder.forProduct4("type", "element_id", "metadata", "text")(PartitionResponse.apply)
  given Encoder[PartitionResponse] =
    Encoder
      .forProduct4("type", "element_id", "metadata", "text")(value =>
        (value.`type`, value.elementId, value.metadata, value.text)
      )

case class Metadata(filename: String, filetype: String, pageNumber: Int)
object Metadata:
  given Decoder[Metadata] =
    Decoder.forProduct3("filename", "filetype", "page_number")(Metadata.apply)
  given Encoder[Metadata] =
    Encoder.forProduct3("filename", "filetype", "page_number")(value =>
      (value.filename, value.filetype, value.pageNumber)
    )

enum Unstructured4sError(val message: String) extends Exception(message):
  case JsonParseError(override val message: String)    extends Unstructured4sError(message)
  case HttpResponseError(override val message: String) extends Unstructured4sError(message)

given Conversion[ResponseException[String, io.circe.Error], Unstructured4sError] with
  def apply(responseException: ResponseException[String, io.circe.Error]): Unstructured4sError =
    responseException match
      case DeserializationException(body, error) =>
        Unstructured4sError.JsonParseError(s"Issue parsing response ${error.getMessage}: $body")
      case HttpError(body, code)                 =>
        Unstructured4sError.HttpResponseError(s"Http status ${code}: $body")

// #unstructured4s_response
case class Unstructured4sResponse[A](result: Either[Unstructured4sError, List[A]])
// #unstructured4s_response
