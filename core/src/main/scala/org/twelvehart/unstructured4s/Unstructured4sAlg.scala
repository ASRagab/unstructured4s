package org.twelvehart.unstructured4s

import org.twelvehart.unstructured4s.model.*
import sttp.model.*

trait Unstructured4sAlg[F[_], A]:
  def partition(
      file: UnstructuredFile,
      request: Unstructured4sRequestFields,
      customHeaders: List[Header]
  ): F[Unstructured4sResponse[A]]

  def partitionMultiple(
      files: Seq[UnstructuredFile],
      request: Unstructured4sRequestFields,
      customHeaders: List[Header]
  ): F[Unstructured4sResponse[List[A]]]
