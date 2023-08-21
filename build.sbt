import org.typelevel.scalacoptions.ScalacOptions

Global / onChangedBuildSource := ReloadOnSourceChanges

Global / excludeLintKeys ++= Set(ThisBuild / pomIncludeRepository, unstructured4s / paradox / sourceDirectory)

ThisBuild / scalaVersion := "3.3.0"

ThisBuild / mimaFailOnNoPrevious := false

ThisBuild / envFileName := ".envrc"

lazy val commonSettings = Seq(
  Test / tpolecatExcludeOptions += ScalacOptions.warnNonUnitStatement
)

addCommandAlias("prepare", "scalafmtAll;test")
addCommandAlias("pushSite", ";project unstructured4s;ghPagesCacheClear;ghpagesPushSite")

lazy val unstructured4s = (project in file("."))
  .enablePlugins(ParadoxSitePlugin, SitePreviewPlugin, ParadoxMaterialThemePlugin, SiteScaladocPlugin, GhpagesPlugin)
  .settings(
    commonSettings,
    CustomTasks.settings,
    publish / skip                   := true,
    Compile / publishArtifact        := false,
    Compile / paradoxMaterialTheme   :=
      ParadoxMaterialTheme()
        .withColor("blue-grey", "orange")
        .withRepository(uri("https://github.com/ASRagab/unstructured4s"))
        .withFont("Overpass", "Overpass Mono")
        .withSocial(
          uri("https://twitter.com/PersistentCooki"),
          uri("https://bsky.app/profile/traversable.bsky.social"),
          uri("https://fosstodon.org/@traversable")
        ),
    Compile / paradoxProperties ++= Map(
      "snip.core.base_dir" -> ((ThisBuild / baseDirectory).value / "core").getAbsolutePath,
      "sttp.version"       -> V.sttp,
      "zio_cats.version"   -> V.zioCats
    ),
    paradox / sourceDirectory        := sourceDirectory.value / "paradox",
    git.remoteRepo                   := scmInfo.value.get.connection.replace("scm:git:", ""),
    Test / tpolecatExcludeOptions += ScalacOptions.warnNonUnitStatement,
    ghpagesCleanSite / excludeFilter :=
      new FileFilter {
        def accept(f: File) = (ghpagesRepository.value / "CNAME").getCanonicalPath == f.getCanonicalPath
      } || "versions.html"
  )
  .aggregate(core, examples)

lazy val core = (project in file("core"))
  .settings(
    commonSettings,
    name                  := "unstructured4s-core",
    libraryDependencies ++= Dependencies.core,
    mimaPreviousArtifacts := previousStableVersion.value.map(organization.value %% name.value % _).toSet
  )

lazy val examples = (project in file("examples"))
  .dependsOn(core)
  .settings(
    commonSettings,
    name                      := "unstructured4s-examples",
    publish / skip            := true,
    Compile / publishArtifact := false,
    libraryDependencies ++= Dependencies.examples
  )
