package org.twelvehart.unstructured4s.examples

import org.twelvehart.unstructured4s.*
import org.twelvehart.unstructured4s.model.*

object BasicApp:
  private val backend = sttp.client3.HttpClientSyncBackend()

  private def program(runMode: RunMode): Either[Throwable, List[PartitionResponse]] =
    for
      apiKey   <- apiKeyEnv
      client    = Unstructured4s.make(backend, ApiKey(apiKey))
      response <- runMode.file.map(file => client.partition(file))
      result   <- response.result
      _         = backend.close()
    yield result

  def main(args: Array[String]): Unit = {
    val runMode = handleArgs(args.toList)
    val result  = program(runMode)
    result match
      case Left(error)  =>
        println(s"Error: ${error.getMessage}")
      case Right(value) =>
        println(s"Result: $value")
  }
