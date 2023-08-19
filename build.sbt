Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "3.3.0"

addCommandAlias("prepare", "scalafmtAll;test")
addCommandAlias("pushSite", ";project unstructured4s;ghPagesCacheClear;ghpagesPushSite")

lazy val commonOptions =
  Seq("-Xfatal-warnings", "-source:future", "-Ykind-projector:underscores", "-Xmax-inlines", "100", "-deprecation")

val commonSettings = Seq(
  scalacOptions ++= commonOptions
)

lazy val unstructured4s = (project in file("."))
  .enablePlugins(ParadoxSitePlugin, SitePreviewPlugin, ParadoxMaterialThemePlugin, SiteScaladocPlugin, GhpagesPlugin)
  .settings(
    commonSettings,
    CustomTasks.settings,
    publish / skip                 := true,
    Compile / publishArtifact      := false,
    Compile / paradoxMaterialTheme :=
      ParadoxMaterialTheme()
        .withColor("blue-grey", "orange")
        .withRepository(uri("https://github.com/ASRagab/unstructured4s"))
        .withFont("Overpass", "Overpass Mono")
        .withSocial(
          uri("https://twitter.com/PersistentCooki"),
          uri("https://bsky.app/profile/traversable.bsky.social"),
          uri("https://fosstodon.org/@traversable")
        ),
    paradox / sourceDirectory      := sourceDirectory.value / "paradox",
    git.remoteRepo                 := scmInfo.value.get.connection.replace("scm:git:", "")
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
