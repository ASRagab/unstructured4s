[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![ci](https://github.com/ASRagab/unstructured4s/workflows/ci/badge.svg)](https://github.com/ASRagab/unstructured4s/actions)
[![Maven Central](https://img.shields.io/maven-central/v/org.twelvehart/unstructured4s-core_3.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22org.twelvehart%22%20AND%20a:%22unstructured4s-core_3%22)
[![Sonatype Snapshots](https://img.shields.io/nexus/s/https/s01.oss.sonatype.org/org.twelvehart/unstructured4s-core_3.svg?label=Sonatype%20Snapshot)](https://s01.oss.sonatype.org/content/repositories/snapshots/org/twelvehart/unstructured4s-core_3)
[![Scaladoc](https://www.javadoc.io/badge/org.twelvehart/unstructured4s-core_3.svg)](https://javadoc.io/doc/org.twelvehart/unstructured4s-core_3)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# unstructured4s

unstructured4s: A Scala 3 wrapper for Unstructured.io

## Getting Started

*IMPORTANT*: You will need git-lfs installed to clone this repository, please see the [git-lfs](https://git-lfs.github.com/)

## Examples

To see examples of how to use this library, please see the examples in the `examples` directory, 
you will need JDK 11+ and sbt available as well.

### Usage

There are three separate versions of the app, that use a different effect ecosystem:

- Cats Effect 3
- ZIO
- Plain Scala 3 (Blocking)

To run the examples, you will need to have an `UNSTRUCTURED_API_KEY` environment variable set to your Unstructured.io
API key.

To run the examples, you can use the following command:

```shell 
UNSTRUCTURED_API_KEY=your_api_key_here "examples/runMain org.twelvehart.unstructured4s.examples.BasicApp"
```

Optionally you can pass one argument to the app which is the type of the sample file it will send to Unstructured.io:

- `png`
- `pdf`

It should print some PartitionResponses to console, and then exit.
