package org.twelvehart.unstructured4s.examples

import cats.implicits.*
import org.twelvehart.unstructured4s.client.*
import org.twelvehart.unstructured4s.client.model.*
import org.twelvehart.unstructured4s.client.model.UnstructuredRequestFields.HiResRequestFields

import java.io.File
import scala.util.Try

object VanillaApp extends App:
  val pdfEither: Either[Throwable, UnstructuredFile] = {
    val currentDir = new File(".").getCanonicalPath
    Try(new File(currentDir, "data/sample.pdf")).map(UnstructuredFile(_)).toEither
  }

  val pngEither: Either[Throwable, UnstructuredFile] = {
    val currentDir = new File(".").getCanonicalPath
    Try(new File(currentDir, "data/sample.png")).map(UnstructuredFile(_)).toEither
  }

  val files     = pdfEither.map(Seq(_)) |+| pngEither.map(Seq(_))
  val apiKeyEnv = sys.env.get("UNSTRUCTURED_API_KEY")

  def program(fileEither: Either[Throwable, Seq[UnstructuredFile]]) =
    for
      files    <- fileEither.left.map(_.getMessage)
      apiKey   <- apiKeyEnv.toRight[String]("UNSTRUCTURED_API_KEY environment variable not set")
      client    = Unstructured4s.simple(ApiKey(apiKey))
      response <- client.multiple(files, HiResRequestFields())
      result   <- response.result
      _         = println(result)
    yield ()

  program(files).fold(println, identity)
