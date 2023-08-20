package org.twelvehart.unstructured4s.examples

import io.circe.*
import io.circe.parser.{*, given}
import org.scalatest.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.twelvehart.unstructured4s.client.*
import org.twelvehart.unstructured4s.client.model.*
import sttp.capabilities
import sttp.client3.*
import sttp.client3.testing.*

import java.io.File
import scala.util.Try

class Unstructured4sSpec extends AnyFlatSpec with Matchers with EitherValues:

  behavior of "Unstructured4s"

  it should "return a List[ResponsePayload]" in:
    val responseJson =
      """[{
      "type": "NarrativeText",
      "element_id": "f140dc965b543fb24347135b15033047",
      "metadata": {
        "filename": "sample.pdf",
        "filetype": "application/pdf",
        "page_number": 1
      },
      "text": "just for use in the Virtual Mechanics tutorials. More text. And more text. And more text. And more text. And more text."
    }]"""

    val testingBackend: SttpBackendStub[Identity, capabilities.WebSockets] =
      SttpBackendStub.synchronous.whenAnyRequest.thenRespond(responseJson)

    val pdfEither: Either[Throwable, UnstructuredFile] = {
      val currentDir = new File(".").getCanonicalPath
      Try(new File(currentDir, "data/sample.pdf")).map(UnstructuredFile(_)).toEither
    }

    val client = Unstructured4s.test(testingBackend, ApiKey("test"))
    pdfEither.map(pdf =>
      val response = client.single(pdf)
      response.result.value `shouldBe` decode[List[ResponsePayload]](responseJson).value
    )

  it should "return a List[List[ResponsePayload]]" in:
    val responseJson =
      """[[{
      "type": "NarrativeText",
      "element_id": "f140dc965b543fb24347135b15033047",
      "metadata": {
        "filename": "sample.pdf",
        "filetype": "application/pdf",
        "page_number": 1
      },
      "text": "just for use in the Virtual Mechanics tutorials. More text. And more text. And more text. And more text. And more text."
    }]]"""

    val testingBackend: SttpBackendStub[Identity, capabilities.WebSockets] =
      SttpBackendStub.synchronous.whenAnyRequest.thenRespond(responseJson)

    val pdfEither: Either[Throwable, UnstructuredFile] = {
      val currentDir = new File(".").getCanonicalPath
      Try(new File(currentDir, "data/sample.pdf")).map(UnstructuredFile(_)).toEither
    }

    val client = Unstructured4s.test(testingBackend, ApiKey("test"))
    pdfEither.map(pdf =>
      val response = client.multiple(Seq(pdf))
      response.result.value `shouldBe` decode[List[List[ResponsePayload]]](responseJson).value
    )
