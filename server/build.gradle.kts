import dependencies.Dependencies.http4k
import dependencies.Dependencies.arrow

plugins {
  application
  kotlin("jvm")
  kotlin("plugin.serialization") version Versions.kotlin
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":engine"))
  implementation(project(":api:dto"))
  implementation(project(":api:structure"))
  implementation(project(":api:http4k"))

  implementation(arrow)
  implementation(http4k("core"))
  implementation(http4k("server-undertow"))
  implementation(http4k("client-apache"))
  implementation(http4k("format-kotlinx-serialization"))

  // TEST
  testImplementation(http4k("client-okhttp"))
}

application {
  mainClass.set("net.lab0.skyscrapers.server.ApplicationKt")
}

tasks {
  test {
    useJUnitPlatform()
  }
}