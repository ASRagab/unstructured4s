# Responses

Every method on the Unstructured4s client returns an `F[Unstructured4sResponse[A]]` where `F[_]` is
the effect capability of your chosen backend.

Requests to [Unstructured] are multipart requests, the return type is always `json` despite the api
allowing `text/csv` as well. This is likely to change in the future, but the recommended
approach is to use the `json` response type.


## `PartitionResponse`

The `PartitionResponse` is a case class that represents the response from the [Unstructured] for a single file.

@@snip[Unstructured4sResponse.scala]($core$/src/main/scala/org/twelvehart/unstructured4s/model/Unstructured4sResponse.scala) { #partition_response }


## `Unstructured4sResponse`

@@snip[Unstructured4sResponse.scala]($core$/src/main/scala/org/twelvehart/unstructured4s/model/Unstructured4sResponse.scala) { #unstructured4s_response }

<br/>

It wraps an `Either[Unstructured4sError, A]` where `A` is either a `PartitionResponse` or `List[PartitionResponse]` 
which depends on whether you called `partition` or `partitionMultiple` respectively.

## `Unstructured4sError`

There exists an implicit conversion between this error type and `ResponseException[String, io.circe.Error]` which is 
error return type from `sttp` there are two kinds of `Unstructured4sError`s

* `JsonParsingError` - This is returned when the response from [Unstructured] is not valid json.
* `HttpResponseError` - This is returned when the response is not `200 OK`.


[Unstructured]: https://unstructured-io.github.io/unstructured/api.html