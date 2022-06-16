import org.gradle.api.JavaVersion

object Versions {
  // java / kotlin
  val java = JavaVersion.VERSION_11.toString()
  val kotlin = "1.6.21"
  val coroutines = "1.4.2"

  // impl libs
  val arrow = "1.0.1"
  val clikt = "3.4.2"
  val http4k = "4.25.14.0"
  val hoplite = "2.1.4"
  val janino = "3.1.6"
  val jline = "3.21.0"
  val kaml = "0.44"
  val kodein = "7.11.0"
  val koin = "3.2.0"
  val kotlinxHtml = "0.7.5"
  val kotlinxSerializationJson = "1.3.3"
  val log4j = "1.7.36"
  val logback = "1.2.11"
  val mu = "2.1.20"
  val sparkJava = "2.9.3"
  val springShell = "2.0.1.RELEASE"

  // test
  val assertJ = "3.22.0"
  val jupiter = "5.8.1"
  val kotest = "5.3.0"
  val kotestArrowJvm = "1.2.5"
  val mockk = "1.12.4"
}
