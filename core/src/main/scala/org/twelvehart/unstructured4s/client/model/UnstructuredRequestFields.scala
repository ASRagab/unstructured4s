package org.twelvehart.unstructured4s.client.model

import sttp.client3.*
import sttp.model.Part

sealed trait UnstructuredRequestFields:
  def toMultipartSequence: List[Part[RequestBody[Any]]]

object UnstructuredRequestFields:
  final case class GeneralRequestFields(
      outputFormat: OutputFormat = OutputFormat.Json,
      xmlKeepTags: Boolean = false,
      encoding: Encoding = Encoding("utf-8"),
      skipInferTableTypes: Option[Vector[String]] = None,
      ocrLanguages: Option[Seq[String]] = None,
      includePageBreaks: Boolean = false,
      ocrStrategy: Option[OCRStrategy] = None
  ) extends UnstructuredRequestFields:
    override def toMultipartSequence: List[Part[RequestBody[Any]]] =
      val ocrLanguagesPart        =
        ocrLanguages.map(langs => multipart("ocr_languages", langs.mkString))
      val skipInferTableTypesPart =
        skipInferTableTypes.map(types => multipart("skip_infer_table_types", types.mkString(",")))
      val ocrStrategyPart         = ocrStrategy.map(_.toMultipart)

      val requiredParts = List(
        outputFormat.toMultipart,
        encoding.toMultipart,
        multipart("include_page_breaks", includePageBreaks.toString),
        multipart("xml_keep_tags", xmlKeepTags.toString)
      )

      val maybeParts = List(ocrLanguagesPart, skipInferTableTypesPart, ocrStrategyPart).flatten

      requiredParts ++ maybeParts

  final case class HiResRequestFields(
      outputFormat: OutputFormat = OutputFormat.Json,
      encoding: Encoding = Encoding("utf-8"),
      pdfInferTableStructure: Boolean = false,
      skipInferTableTypes: Vector[String] = Vector.empty,
      includePageBreaks: Boolean = false,
      hiResModelName: Option[HiResModelName] = None,
      ocrLanguages: Option[Seq[String]] = None,
      xmlKeepTags: Option[Boolean] = None
  ) extends UnstructuredRequestFields:
    override def toMultipartSequence: List[Part[RequestBody[Any]]] =
      val ocrLanguagesPart        =
        ocrLanguages.map(langs => multipart("ocr_languages", langs.mkString))
      val skipInferTableTypesPart =
        skipInferTableTypes.map(types => multipart("skip_infer_table_types", types.mkString(",")))
      val hiResModelNamePart      = hiResModelName.map(_.toMultipart)
      val ocrStrategyPart         = multipart("ocr_strategy", OCRStrategy.HiRes.value)

      val requiredParts = List(
        outputFormat.toMultipart,
        encoding.toMultipart,
        ocrStrategyPart,
        multipart("include_page_breaks", includePageBreaks.toString),
        multipart("xml_keep_tags", xmlKeepTags.toString)
      )
      val maybeParts    = List(ocrLanguagesPart, skipInferTableTypesPart, hiResModelNamePart).flatten

      requiredParts ++ maybeParts
