package org.twelvehart.unstructured4s.examples

import org.twelvehart.unstructured4s.*
import org.twelvehart.unstructured4s.model.*

object BasicApp extends App:
  private val backend = sttp.client3.HttpClientSyncBackend()

  private val program =
    for
      file    <- pdfEither
      apiKey  <- apiKeyEnv
      client   = Unstructured4s.make(backend, ApiKey(apiKey))
      response = client.partition(file)
      result  <- response.result
      _        = backend.close()
    yield result

  program.fold(
    err => println(s"${err.getMessage}"),
    res => println(s"$res")
  )
