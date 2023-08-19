package org.twelvehart.unstructured4s.client

import sttp.client3.*
import sttp.model.{Part, Uri}

import java.io.File

trait CanMultipart[T]:
  extension (t: T) def toMultipart: Part[RequestBody[Any]]

object Model:
  opaque type ApiKey <: String = String
  object ApiKey:
    def apply(value: String): ApiKey = value

  opaque type UnstructuredFile <: File = File
  object UnstructuredFile:
    def apply(value: File): UnstructuredFile = value

  opaque type Encoding <: String = String
  object Encoding:
    def apply(value: String): Encoding = value

  enum OCRStrategy(val value: String):
    case HiRes   extends OCRStrategy("hi_res")
    case Fast    extends OCRStrategy("fast")
    case OCROnly extends OCRStrategy("ocr_only")
    case Auto    extends OCRStrategy("auto")

  enum OutputFormat(val value: String):
    case Json       extends OutputFormat("json")
    case `Text/Csv` extends OutputFormat("text/csv")

  sealed trait Endpoint(value: Uri):
    case object V0              extends Endpoint(uri"https://api.unstructured.io/general/v0/general")
    case class Custom(uri: Uri) extends Endpoint(uri)

  enum HiResModelName(val value: String):
    case Chipper extends HiResModelName("chipper")

  sealed trait RequestPayload:
    def toMultipartSequence: Seq[Part[RequestBody[Any]]]

  final case class GeneralPayload(
      files: Seq[UnstructuredFile],
      outputFormat: OutputFormat = OutputFormat.Json,
      xmlKeepTags: Boolean = false,
      encoding: Encoding = Encoding("utf-8"),
      skipInferTableTypes: Option[Vector[String]] = None,
      ocrLanguages: Option[Seq[String]] = None,
      includePageBreaks: Boolean = false,
      ocrStrategy: Option[OCRStrategy] = None
  ) extends RequestPayload:
    override def toMultipartSequence: Seq[Part[RequestBody[Any]]] =
      val ocrLanguagesPart        =
        ocrLanguages.map(langs => multipart("ocr_languages", langs.mkString))
      val skipInferTableTypesPart =
        skipInferTableTypes.map(types => multipart("skip_infer_table_types", types.mkString(",")))
      val ocrStrategyPart         = ocrStrategy.map(_.toMultipart)

      val fileParts     = files.map(_.toMultipart)
      val requiredParts = Seq(
        outputFormat.toMultipart,
        encoding.toMultipart,
        multipart("include_page_breaks", includePageBreaks.toString),
        multipart("xml_keep_tags", xmlKeepTags.toString)
      )

      val maybeParts = List(ocrLanguagesPart, skipInferTableTypesPart, ocrStrategyPart).flatten

      fileParts ++ requiredParts ++ maybeParts

  final case class HiResPayload(
      files: Seq[UnstructuredFile],
      outputFormat: OutputFormat = OutputFormat.Json,
      encoding: Encoding = Encoding("utf-8"),
      pdfInferTableStructure: Boolean = false,
      skipInferTableTypes: Vector[String] = Vector.empty,
      includePageBreaks: Boolean = false,
      hiResModelName: Option[HiResModelName] = None,
      ocrLanguages: Option[Seq[String]] = None,
      xmlKeepTags: Option[Boolean] = None
  ) extends RequestPayload:
    override def toMultipartSequence: Seq[Part[RequestBody[Any]]] =
      val ocrLanguagesPart        =
        ocrLanguages.map(langs => multipart("ocr_languages", langs.mkString))
      val skipInferTableTypesPart =
        skipInferTableTypes.map(types => multipart("skip_infer_table_types", types.mkString(",")))
      val hiResModelNamePart      = hiResModelName.map(_.toMultipart)
      val ocrStrategyPart         = multipart("ocr_strategy", OCRStrategy.HiRes.value)

      val fileParts     = files.map(_.toMultipart).toList
      val requiredParts = List(
        outputFormat.toMultipart,
        encoding.toMultipart,
        ocrStrategyPart,
        multipart("include_page_breaks", includePageBreaks.toString),
        multipart("xml_keep_tags", xmlKeepTags.toString)
      )
      val maybeParts    = List(ocrLanguagesPart, skipInferTableTypesPart, hiResModelNamePart).flatten

      fileParts ::: requiredParts ::: maybeParts

end Model

given CanMultipart[ApiKey] with
  extension (t: ApiKey)
    def toMultipart: Part[RequestBody[Any]] = multipart("unstructured_api_key", t)

given CanMultipart[OCRStrategy] with
  extension (t: OCRStrategy)
    def toMultipart: Part[RequestBody[Any]] = multipart("ocr_strategy", t.value)

given CanMultipart[UnstructuredFile] with
  extension (t: UnstructuredFile)
    def toMultipart: Part[RequestBody[Any]] = multipartFile("files", t)

given CanMultipart[HiResModelName] with
  extension (t: HiResModelName)
    def toMultipart: Part[RequestBody[Any]] = multipart("hi_res_model_name", t.value)

given CanMultipart[Encoding] with
  extension (t: Encoding) def toMultipart: Part[RequestBody[Any]] = multipart("encoding", t)

given CanMultipart[OutputFormat] with
  extension (t: OutputFormat)
    def toMultipart: Part[RequestBody[Any]] = multipart("output_format", t.value)
