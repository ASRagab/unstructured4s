# Making Requests

The [Unstructured.io API] takes multipart form requests. There are currently two types of requests one can make with the
Unstructured4s client:

[Unstructured.io API]: https://unstructured-io.github.io/unstructured/api.html

## partition

The `partition` request is used to send one file to be partitioned by Unstructured.io. The request takes three
parameters,
only one of which does not have a default value:

* `file` - The file to be partitioned, of type UnstructuredFile, just an `opaque type` for `java.io.File`
* `request` - `Unstructured4sRequestFields` The request fields to be sent to Unstructured.io. These can be of two types:
    * `GeneralRequestFields`
    * `HiResRequestFields`
* `customHeaders` - List[Header] - A list of custom `sttp` headers to be sent with the request. These will override
  any default headers including the `unstructured-api-key` header.

A hopefully sensible default with all necessary parameters for each is provided in api client.

<br/>

@@snip [domain.scala]($core$/src/main/scala/org/twelvehart/unstructured4s/model/domain.scala) { #general_request_fields }


<br/>

@@snip [domain.scala]($core$/src/main/scala/org/twelvehart/unstructured4s/model/domain.scala) { #hires_request_fields }

<br/>
Then to make the request:

```scala
response = client.partition(file) // can provide a request and headers here as well
```

<br/>
## partitionMultiple

The `partitionMultiple` request is used to send many files to be partitioned by Unstructured.io. It is the same as the
`partition` request except that

* `files` - The files to be partitioned, of type `Seq[UnstructuredFile]`

<br/>

Similarly:

```scala
response = client.partitionMultiple(files) // can provide a request and headers here as well
```


