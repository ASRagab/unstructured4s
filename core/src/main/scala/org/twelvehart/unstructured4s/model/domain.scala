package org.twelvehart.unstructured4s.model

import org.twelvehart.unstructured4s.model.OCRStrategy.Auto
import sttp.client3.*
import sttp.model.Uri
import sttp.model.Part

import scala.annotation.unused
import java.io.File

export CanMultipart.given
export Unstructured4sRequestFields.*

opaque type ApiKey <: String = String
object ApiKey:
  def apply(value: String): ApiKey = value

opaque type UnstructuredFile <: File = File
object UnstructuredFile:
  def apply(value: File): UnstructuredFile = value

opaque type Encoding <: String = String
object Encoding:
  def apply(value: String): Encoding = value

val `UTF-8` = Encoding("utf-8")

enum OCRStrategy(val value: String):
  case HiRes   extends OCRStrategy("hi_res")
  case Fast    extends OCRStrategy("fast")
  case OCROnly extends OCRStrategy("ocr_only")
  case Auto    extends OCRStrategy("auto")

enum OutputFormat(val value: String):
  case Json extends OutputFormat("json")

  @unused("csv output is not yet supported by the api") case `Text/Csv` extends OutputFormat("text/csv")

sealed trait Endpoint(val value: Uri):
  case object V0              extends Endpoint(uri"https://api.unstructured.io/general/v0/general")
  case class Custom(uri: Uri) extends Endpoint(uri)

enum HiResModelName(val value: String):
  case Chipper extends HiResModelName("chipper")

sealed trait Unstructured4sRequestFields:
  def toMultipartSequence: List[Part[RequestBody[Any]]]

object Unstructured4sRequestFields:
  private val CoordinatesField            = "coordinates"
  private val XmlKeepTagsField            = "xml_keep_tags"
  private val IncludePageBreaksField      = "include_page_breaks"
  private val SkipInferTableTypesField    = "skip_infer_table_types"
  private val OcrLanguagesField           = "ocr_languages"
  private val PdfInferTableStructureField = "pdf_infer_table_structure"

  final case class GeneralRequestFields(
      // #general_request_fields
      outputFormat: OutputFormat = OutputFormat.Json,
      xmlKeepTags: Boolean = false,
      coordinates: Boolean = false,
      encoding: Encoding = `UTF-8`,
      skipInferTableTypes: Option[Vector[String]] = None,
      ocrLanguages: Option[Seq[String]] = None,
      includePageBreaks: Boolean = false,
      ocrStrategy: OCRStrategy = OCRStrategy.Auto
      // #general_request_fields
  ) extends Unstructured4sRequestFields:

    override def toMultipartSequence: List[Part[RequestBody[Any]]] =
      val requiredParts = List(
        outputFormat.toMultipart,
        multipart(XmlKeepTagsField, xmlKeepTags.toString),
        multipart(CoordinatesField, coordinates.toString),
        encoding.toMultipart,
        multipart(IncludePageBreaksField, includePageBreaks.toString),
        ocrStrategy.toMultipart
      )

      val skipInferTableTypesPart =
        skipInferTableTypes.map(types => multipart(SkipInferTableTypesField, types.mkString("[", ",", "]")))
      val ocrLanguagesPart        =
        ocrLanguages.map(langs => multipart(OcrLanguagesField, langs.mkString))

      val maybeParts = List(ocrLanguagesPart, skipInferTableTypesPart).flatten
      requiredParts ++ maybeParts
  end GeneralRequestFields

  final case class HiResRequestFields(
      // #hires_request_fields
      outputFormat: OutputFormat = OutputFormat.Json,
      encoding: Encoding = `UTF-8`,
      coordinates: Boolean = false,
      pdfInferTableStructure: Boolean = false,
      skipInferTableTypes: Option[Vector[String]] = None,
      includePageBreaks: Boolean = false,
      hiResModelName: Option[HiResModelName] = None,
      ocrLanguages: Option[Seq[String]] = None
      // #hires_request_fields
  ) extends Unstructured4sRequestFields:

    override def toMultipartSequence: List[Part[RequestBody[Any]]] =
      val ocrLanguagesPart        =
        ocrLanguages.map(langs => multipart(OcrLanguagesField, langs.mkString))
      val skipInferTableTypesPart =
        skipInferTableTypes.map(types => multipart(SkipInferTableTypesField, types.mkString("[", ",", "]")))
      val hiResModelNamePart      = hiResModelName.map(_.toMultipart)

      val requiredParts = List(
        outputFormat.toMultipart,
        encoding.toMultipart,
        multipart(CoordinatesField, coordinates.toString),
        multipart(PdfInferTableStructureField, pdfInferTableStructure.toString),
        OCRStrategy.HiRes.toMultipart,
        multipart(IncludePageBreaksField, includePageBreaks.toString)
      )
      val maybeParts    = List(ocrLanguagesPart, skipInferTableTypesPart, hiResModelNamePart).flatten
      requiredParts ++ maybeParts
  end HiResRequestFields

end Unstructured4sRequestFields
