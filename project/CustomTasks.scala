import sbt._
import Keys._
import java.nio.file.{Files, Paths}
import scala.util.Try

object CustomTasks {
  val ghPagesCacheClear    = taskKey[Unit]("ghPagesCacheClear")
  val ghPagesCacheLocation = settingKey[String]("ghPagesCacheLocation")

  lazy val settings: Seq[Setting[_]] = Seq(
    ghPagesCacheLocation := "~/.sbt/ghpages",
    ghPagesCacheClear    := {
      val path = Paths.get(ghPagesCacheLocation.value)

      def deleteRecursively(file: java.nio.file.Path): Unit = {
        if (Files.isDirectory(file)) {
          Files.list(file).forEach(deleteRecursively)
        }
        Files.delete(file)
      }

      Def.taskIf(
        if (Files.exists(path)) {
          Try(deleteRecursively(path)).recover { case e: Exception =>
            streams.value.log.error(s"Error deleting folder: ${e.getMessage}")
          }
        } else {
          streams.value.log.info(s"The folder '${ghPagesCacheLocation.value}' does not exist.")
        }
      )
    }
  )
}
