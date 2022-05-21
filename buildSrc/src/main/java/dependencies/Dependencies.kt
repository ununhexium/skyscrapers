package dependencies

import Versions

object Dependencies {
  val arrow = "io.arrow-kt:arrow-core:${Versions.arrow}"

  val clikt = "com.github.ajalt.clikt:clikt:${Versions.clikt}"

  fun http4k(part: String) =
    "org.http4k:http4k-$part:${Versions.http4k}"

  object http4k {
    val core = http4k("core")
    val clientOkhttp = http4k("client-okhttp")
    val formatKotlinxSerialization = http4k("format-kotlinx-serialization")
    val serverUndertow = http4k("server-undertow")
  }

  val jline = "org.jline:jline:${Versions.jline}"
  val jlineBuiltins = "org.jline:jline-builtins:${Versions.jline}"

  val kotlinxHtml = "org.jetbrains.kotlinx:kotlinx-html:${Versions.kotlinxHtml}"
  val kotlinxHtmlJvm =
    "org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.kotlinxHtml}"

  val kotlinxSerializationJson =
    "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerializationJson}"

  val sparkJava = "com.sparkjava:spark-core:${Versions.sparkJava}"

}
