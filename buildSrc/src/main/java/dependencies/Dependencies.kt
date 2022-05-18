package dependencies

import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
  //  val material = "com.google.android.material:material:${Versions.material}"
  val clikt = "com.github.ajalt.clikt:clikt:${Versions.clikt}"

  fun http4k(part: String) =
    "org.http4k:http4k-$part:${Versions.http4k}"

  val jline = "org.jline:jline:${Versions.jline}"
  val jlineBuiltins = "org.jline:jline-builtins:${Versions.jline}"

  val kotlinxHtml = "org.jetbrains.kotlinx:kotlinx-html:${Versions.kotlinxHtml}"
  val kotlinxHtmlJvm =
    "org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.kotlinxHtml}"

  val kotlinxSerializationJson =
    "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerializationJson}"

  val sparkJava = "com.sparkjava:spark-core:${Versions.sparkJava}"

}
