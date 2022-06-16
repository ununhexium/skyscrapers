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

  object janino {
    val janino = "org.codehaus.janino:janino:${Versions.janino}"
    val commonsCompiler = "org.codehaus.janino:commons-compiler:${Versions.janino}"
  }

  val jline = "org.jline:jline:${Versions.jline}"
  val jlineBuiltins = "org.jline:jline-builtins:${Versions.jline}"


  val junit5Api = "org.junit.jupiter:junit-jupiter-api:${Versions.jupiter}"
  val junit5Params =
    "org.junit.jupiter:junit-jupiter-params:${Versions.jupiter}"
  val junit5Engine =
    "org.junit.jupiter:junit-jupiter-engine:${Versions.jupiter}"

  val kaml = "com.charleskorn.kaml:kaml:${Versions.kaml}"

  val kodein = "org.kodein.di:kodein-di-jvm:${Versions.kodein}"

  object koin {
    val core = "io.insert-koin:koin-core:${Versions.koin}"
    val junit4 = "io.insert-koin:koin-test-junit4:${Versions.koin}"
    val junit5 = "io.insert-koin:koin-test-junit5:${Versions.koin}"
    val test = "io.insert-koin:koin-test:${Versions.koin}"
  }

  object kotest {
    val core = "io.kotest:kotest-assertions-core:${Versions.kotest}"
    val arrowJvm =
      "io.kotest.extensions:kotest-assertions-arrow-jvm:${Versions.kotestArrowJvm}"
  }

  val kotlinxHtml = "org.jetbrains.kotlinx:kotlinx-html:${Versions.kotlinxHtml}"
  val kotlinxHtmlJvm =
    "org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.kotlinxHtml}"

  val kotlinxSerializationJson =
    "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerializationJson}"

  object log4j {
    val simple = "org.slf4j:slf4j-simple:${Versions.log4j}"
  }

  object logback {
    val classic = "ch.qos.logback:logback-classic:${Versions.logback}"
    val core = "ch.qos.logback:logback-core:${Versions.logback}"
  }

  val mockK = "io.mockk:mockk:${Versions.mockk}"

  val mu = "io.github.microutils:kotlin-logging-jvm:${Versions.mu}"

  object spring {
    val shell = "org.springframework.shell:spring-shell-starter:${Versions.springShell}"
  }

  val sparkJava = "com.sparkjava:spark-core:${Versions.sparkJava}"
}
