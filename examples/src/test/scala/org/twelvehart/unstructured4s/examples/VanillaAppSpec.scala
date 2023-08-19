package org.twelvehart.unstructured4s.examples

import io.circe.*
import io.circe.parser.{*, given}
import org.scalatest.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.twelvehart.unstructured4s.client.*
import sttp.capabilities
import sttp.client3.*
import sttp.client3.circe.*
import sttp.client3.testing.*

import java.io.File

class VanillaAppSpec extends AnyFlatSpec with Matchers with EitherValues:
  val sampleJson                                                         = """{"key": "value"}""".stripMargin
  val testingBackend: SttpBackendStub[Identity, capabilities.WebSockets] =
    SttpBackendStub.synchronous.whenAnyRequest.thenRespond(sampleJson)

  given responseAs: ResponseAs[Either[DeserializationException[Error], Json], Any] =
    asJsonAlways[Json]

  behavior of "VanillaApp"

  it should "return a JsonString" in:
    val file: UnstructuredFile =
      UnstructuredFile(new File(getClass.getClassLoader.getResource("sample.pdf").getFile))

    val client   = Unstructured4s.synchronous(testingBackend, ApiKey("test"))
    val response = client.post(HiResPayload(Seq(file)))
    response.body.value `shouldBe` parse(sampleJson).value
