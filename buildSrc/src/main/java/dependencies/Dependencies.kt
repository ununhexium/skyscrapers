package dependencies

import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
  //  val material = "com.google.android.material:material:${Versions.material}"
  val clikt = "com.github.ajalt.clikt:clikt:${Versions.clikt}"

  val jline = "org.jline:jline:${Versions.jline}"
  val jlineBuiltins = "org.jline:jline-builtins:${Versions.jline}"

  val kotlinxHtml = "org.jetbrains.kotlinx:kotlinx-html:${Versions.kotlinxHtml}"
  val kotlinxHtmlJvm = "org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.kotlinxHtml}"

  val kotlinxSerializationJson =
    "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerializationJson}"

  val sparkJava = "com.sparkjava:spark-core:${Versions.sparkJava}"

  fun DependencyHandler.impl(dep: String) {
    add("implementation", dep)
  }

  fun DependencyHandler.unsafeImpl(dep: Any) {
    add("implementation", dep)
  }

  fun DependencyHandler.clikt() {
    impl(Dependencies.clikt)
  }

  fun DependencyHandler.jline() {
    impl(Dependencies.jline)
  }

  fun DependencyHandler.jlineBuiltins() {
    impl(Dependencies.jlineBuiltins)
  }

  fun DependencyHandler.kotlinxHtml() {
    impl(kotlinxHtml)
    impl(kotlinxHtmlJvm)
  }

  fun DependencyHandler.kotlinxSerializationJson() {
    impl(kotlinxSerializationJson)
  }

  fun DependencyHandler.sparkJava() {
    impl(Dependencies.sparkJava)
  }

  fun DependencyHandler.http4k() {
    unsafeImpl(platform("org.http4k:http4k-bom:${Versions.http4k}"))
    impl("org.http4k:http4k-core")
    impl("org.http4k:http4k-server-undertow")
    impl("org.http4k:http4k-client-apache")
    impl("org.http4k:http4k-format-kotlinx-serialization")
  }
}
