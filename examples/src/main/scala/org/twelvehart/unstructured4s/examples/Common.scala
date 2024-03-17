package org.twelvehart.unstructured4s.examples

import cats.data.NonEmptyList
import cats.implicits.*
import org.twelvehart.unstructured4s.model.*

import java.io.File

export Common.*

object Common:
  enum RunMode(val file: Either[Throwable, UnstructuredFile]):
    case SinglePdf extends RunMode(pdfEither)
    case SinglePng extends RunMode(pngEither)

  val pdfEither: Either[Throwable, UnstructuredFile] = {
    val currentDir = new File(".").getCanonicalPath
    val file       = new File(currentDir, "data/sample.pdf")
    Either.cond(file.exists, UnstructuredFile(file), new Exception(s"File not found at ${file.getCanonicalPath}"))
  }

  val pngEither: Either[Throwable, UnstructuredFile] = {
    val currentDir = new File(".").getCanonicalPath
    val file       = new File(currentDir, "data/sample.png")
    Either.cond(file.exists, UnstructuredFile(file), new Exception(s"File not found at ${file.getCanonicalPath}"))
  }

  val filesEither: NonEmptyList[Either[Throwable, UnstructuredFile]] =
    NonEmptyList(pdfEither, List(pngEither))

  val apiKeyEnv: Either[Throwable, String] =
    sys.env.get("UNSTRUCTURED_API_KEY").toRight(new Exception("UNSTRUCTURED_API_KEY not set"))

  def handleArgs(args: List[String]): RunMode =
    args match
      case _ :: "pdf" :: _ => RunMode.SinglePdf
      case _ :: "png" :: _ => RunMode.SinglePng
      case _               => RunMode.SinglePng
