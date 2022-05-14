package dependencies

import dependencies.Dependencies.impl
import dependencies.Dependencies.unsafeImpl
import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {
  //  val material = "com.google.android.material:material:${Versions.material}"
  val clikt = "com.github.ajalt.clikt:clikt:${Versions.clikt}"

  val jline = "org.jline:jline:${Versions.jline}"
  val jlineBuiltins = "org.jline:jline-builtins:${Versions.jline}"

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
    impl("org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.kotlinxHtml}")
    impl("org.jetbrains.kotlinx:kotlinx-html:${Versions.kotlinxHtml}")
  }

  fun DependencyHandler.sparkJava() {
    impl(Dependencies.sparkJava)
  }

  fun DependencyHandler.http4k() {
    unsafeImpl(platform("org.http4k:http4k-bom:${Versions.http4k}"))
    impl("org.http4k:http4k-core")
    impl("org.http4k:http4k-server-undertow")
    impl("org.http4k:http4k-client-apache")
  }
}