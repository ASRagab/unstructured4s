package org.twelvehart.unstructured4s.client.model

import io.circe.*
import io.circe.generic.semiauto.*

case class ResponsePayload(`type`: String, elementId: String, metadata: Metadata, text: String)
object ResponsePayload:
  given Decoder[ResponsePayload] =
    Decoder.forProduct4("type", "element_id", "metadata", "text")(ResponsePayload.apply)
  given Encoder[ResponsePayload] =
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
