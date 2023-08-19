ThisBuild / organization         := "org.twelvehart"
ThisBuild / organizationName     := "Twelve Hart Industries"
ThisBuild / organizationHomepage := None

sonatypeBundleDirectory := (ThisBuild / baseDirectory).value / target.value.getName / "sonatype-staging" / (ThisBuild / version).value

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/ASRagab/unstructured4s"),
    "scm:git@github.com:ASRagab/unstructured4s.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "ASRagab",
    name = "Ahmad Ragab",
    email = "hegemon@twelvehart.org",
    url = url("https://github.com/ASRagab")
  )
)

ThisBuild / description := "Unstructured4s is a Scala library for working with unstructured.io API"
ThisBuild / licenses    := List("The MIT License" -> new URL("https://opensource.org/license/mit/"))
ThisBuild / homepage    := None

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository     := "https://s01.oss.sonatype.org/service/local"

ThisBuild / versionScheme := Some("early-semver")
