package dependencies

import Versions

object Dependencies {
  val arrow = "io.arrow-kt:arrow-core:${Versions.arrow}"

  val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"

  val clikt = "com.github.ajalt.clikt:clikt:${Versions.clikt}"

  object hoplite {
    val core = "com.sksamuel.hoplite:hoplite-core:${Versions.hoplite}"
    val json = "com.sksamuel.hoplite:hoplite-json:${Versions.hoplite}"
  }

  fun http4k(part: String) =
    "org.http4k:http4k-$part:${Versions.http4k}"

  object http4k {
    val core = http4k("core")
    val clientOkhttp = http4k("client-okhttp")
    val formatKotlinxSerialization = http4k("format-kotlinx-serialization")
    val serverUndertow = http4k("server-undertow")
    val kotest = http4k("testing-kotest")
  }

  val jline = "org.jline:jline:${Versions.jline}"
  val jlineBuiltins = "org.jline:jline-builtins:${Versions.jline}"


  val junit5Api = "org.junit.jupiter:junit-jupiter-api:${Versions.jupiter}"
  val junit5Params = "org.junit.jupiter:junit-jupiter-params:${Versions.jupiter}"
  val junit5Engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.jupiter}"

  val kaml = "com.charleskorn.kaml:kaml:${Versions.kaml}"

  val kodein = "org.kodein.di:kodein-di-jvm:${Versions.kodein}"


  object kotest {
    val core = "io.kotest:kotest-assertions-core:${Versions.kotest}"
    val arrowJvm = "io.kotest.extensions:kotest-assertions-arrow-jvm:${Versions.kotestArrowJvm}"
  }

  val kotlinxHtml = "org.jetbrains.kotlinx:kotlinx-html:${Versions.kotlinxHtml}"
  val kotlinxHtmlJvm =
    "org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.kotlinxHtml}"

  val kotlinxSerializationJson =
    "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerializationJson}"

  val mockK = "io.mockk:mockk:${Versions.mockk}"

  val sparkJava = "com.sparkjava:spark-core:${Versions.sparkJava}"

}
