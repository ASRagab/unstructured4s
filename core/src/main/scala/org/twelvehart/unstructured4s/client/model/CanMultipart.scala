package org.twelvehart.unstructured4s.client.model

import sttp.client3.{RequestBody, multipart, multipartFile}
import sttp.model.Part

trait CanMultipart[T]:
  extension (t: T) def toMultipart: Part[RequestBody[Any]]

given CanMultipart[ApiKey] with
  extension (t: ApiKey) def toMultipart: Part[RequestBody[Any]] = multipart("unstructured_api_key", t)

given CanMultipart[OCRStrategy] with
  extension (t: OCRStrategy) def toMultipart: Part[RequestBody[Any]] = multipart("ocr_strategy", t.value)

given CanMultipart[UnstructuredFile] with
  extension (t: UnstructuredFile) def toMultipart: Part[RequestBody[Any]] = multipartFile("files", t)

given CanMultipart[HiResModelName] with
  extension (t: HiResModelName) def toMultipart: Part[RequestBody[Any]] = multipart("hi_res_model_name", t.value)

given CanMultipart[Encoding] with
  extension (t: Encoding) def toMultipart: Part[RequestBody[Any]] = multipart("encoding", t)

given CanMultipart[OutputFormat] with
  extension (t: OutputFormat) def toMultipart: Part[RequestBody[Any]] = multipart("output_format", t.value)
