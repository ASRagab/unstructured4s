package org.twelvehart.unstructured4s.examples

import io.circe.*
import org.twelvehart.unstructured4s.client.*
import sttp.client3.circe.asJsonAlways
import sttp.client3.{DeserializationException, ResponseAs}

import java.io.File

object VanillaApp extends App:
  val file: UnstructuredFile =
    UnstructuredFile(new File(getClass.getClassLoader.getResource("sample.pdf").getFile))

  given responseAs: ResponseAs[Either[DeserializationException[Error], Json], Any] =
    asJsonAlways[Json]

  val client   = Unstructured4s.simple(ApiKey(""))
  val response = client.post(HiResPayload(Seq(file)))
  response.body.fold(println, println)
