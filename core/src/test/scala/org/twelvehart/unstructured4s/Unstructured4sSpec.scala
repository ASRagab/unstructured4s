package org.twelvehart.unstructured4s

import io.circe.*
import io.circe.parser.*
import org.scalatest.*
import org.scalatest.flatspec.AnyFlatSpec
import org.twelvehart.unstructured4s.model.*
import sttp.capabilities
import sttp.client3.*
import sttp.client3.testing.*

class Unstructured4sSpec extends DefaultSpec:

  behavior of "Unstructured4s"

  it should "return a List[PartitionResponse]" in:
    val responseJson =
      """
        |[{
        |      "type": "NarrativeText",
        |      "element_id": "f140dc965b543fb24347135b15033047",
        |      "metadata": {
        |        "filename": "sample.pdf",
        |        "filetype": "application/pdf",
        |        "page_number": 1
        |      },
        |      "text": "just for use in the Virtual Mechanics tutorials. More text. And more text. And more text. And more text. And more text."
        |    }]
        |""".stripMargin

    val testingBackend: SttpBackendStub[Identity, capabilities.WebSockets] =
      SttpBackendStub.synchronous.whenAnyRequest.thenRespond(responseJson)

    val client = Unstructured4s.make(testingBackend, ApiKey("test"))
    pdfEither.map(pdf =>
      val response = client.partition(pdf)
      response.result.value `shouldBe` decode[List[PartitionResponse]](responseJson).value
    )

  it should "return a List[List[PartitionResponse]]" in:
    val responseJson =
      """
        |[[{
        |      "type": "NarrativeText",
        |      "element_id": "f140dc965b543fb24347135b15033047",
        |      "metadata": {
        |        "filename": "sample.pdf",
        |        "filetype": "application/pdf",
        |        "page_number": 1
        |      },
        |      "text": "just for use in the Virtual Mechanics tutorials. More text. And more text. And more text. And more text. And more text."
        |    }]]
        |""".stripMargin

    val testingBackend: SttpBackendStub[Identity, capabilities.WebSockets] =
      SttpBackendStub.synchronous.whenAnyRequest.thenRespond(responseJson)

    val client = Unstructured4s.make(testingBackend, ApiKey("test"))

    pdfEither.map(pdf =>
      val response = client.partitionMultiple(Seq(pdf))
      response.result.value `shouldBe` decode[List[List[PartitionResponse]]](responseJson).value
    )

  it should "create a set of headers" in:
    val apiKey  = ApiKey("test")
    val headers = Unstructured4s.apiKeyHeader(apiKey) :: Unstructured4s.defaultHeaders
    headers.size `shouldBe` 3
    headers.exists(_.name == "unstructured-api-key") `shouldBe` true
    headers.exists(_.name == "Content-Type") `shouldBe` true
    headers.exists(_.name == "Accept") `shouldBe` true
