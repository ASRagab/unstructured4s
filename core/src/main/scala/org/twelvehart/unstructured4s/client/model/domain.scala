package org.twelvehart.unstructured4s.client.model

import sttp.client3.*
import sttp.model.Uri

export RequestOptions.*
import java.io.File

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
