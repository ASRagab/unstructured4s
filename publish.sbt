ThisBuild / organization         := "org.twelvehart"
ThisBuild / organizationName     := "Twelve Hart Industries"
ThisBuild / organizationHomepage := Some(url("https://twelvehart.org"))

sonatypeBundleDirectory := (ThisBuild / baseDirectory).value / target.value.getName / "sonatype-staging" / (ThisBuild / version).value

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/ASRagab/unstructured4s"),
    "scm:git:git@github.com:ASRagab/unstructured4s.git"
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
ThisBuild / licenses    := List("The MIT License" -> url("https://opensource.org/license/mit/"))
ThisBuild / homepage    := Some(url("https://unstructured4s.twelvehart.org/"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / sonatypeRepository     := "https://s01.oss.sonatype.org/service/local"

ThisBuild / versionScheme := Some("early-semver")
