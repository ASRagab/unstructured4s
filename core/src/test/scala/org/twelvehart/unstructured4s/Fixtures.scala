package org.twelvehart.unstructured4s

import model.*
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.File
import scala.util.Try

trait Unstructured4sFixture:
  lazy val pdfEither: Either[Throwable, UnstructuredFile] = {
    val currentDir = new File(".").getCanonicalPath
    Try(new File(currentDir, "data/sample.pdf")).map(UnstructuredFile(_)).toEither
  }

trait DefaultSpec extends AnyFlatSpec with Matchers with EitherValues with Unstructured4sFixture
