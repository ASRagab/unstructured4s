package org.twelvehart.unstructured4s.examples

import cats.implicits.*
import org.twelvehart.unstructured4s.model.*

import java.io.File
import scala.util.Try

export Common.*

object Common:
  val pdfEither: Either[Throwable, UnstructuredFile] = {
    val currentDir = new File(".").getCanonicalPath
    Try(new File(currentDir, "data/sample.pdf")).map(UnstructuredFile(_)).toEither
  }

  val pngEither: Either[Throwable, UnstructuredFile] = {
    val currentDir = new File(".").getCanonicalPath
    Try(new File(currentDir, "data/sample.png")).map(UnstructuredFile(_)).toEither
  }

  val filesEither: Either[Throwable, Seq[UnstructuredFile]] =
    pdfEither.map(Seq(_)) |+| pngEither.map(Seq(_))

  val apiKeyEnv: Either[Throwable, String] =
    sys.env.get("UNSTRUCTURED_API_KEY").toRight(new Exception("UNSTRUCTURED_API_KEY not set"))
