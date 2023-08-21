package org.twelvehart.unstructured4s.examples

import cats.implicits.*
import org.twelvehart.unstructured4s.model.*

import java.io.File

export Common.*

object Common:
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

  val filesEither: Either[Throwable, Seq[UnstructuredFile]] =
    pdfEither.map(Seq(_)) |+| pngEither.map(Seq(_))

  val apiKeyEnv: Either[Throwable, String] =
    sys.env.get("UNSTRUCTURED_API_KEY").toRight(new Exception("UNSTRUCTURED_API_KEY not set"))
