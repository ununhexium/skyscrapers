import dependencies.Dependencies.arrow
import dependencies.Dependencies.http4k
import dependencies.Dependencies.logback
import dependencies.Dependencies.mu
import dependencies.Dependencies.swaggerUi

plugins {
  application
  kotlin("jvm")
  kotlin("plugin.serialization") version Versions.kotlin
}

group = "net.lab0.skyscrapers"

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":api:dto"))
  implementation(project(":api:structure"))
  implementation(project(":api:http4k"))
  implementation(project(":engine"))

  implementation(arrow)
  implementation(http4k.core)
  implementation(http4k.serverUndertow)
  implementation(http4k.contract)
  implementation(http4k("client-apache"))
  implementation(http4k.formatKotlinxSerialization)
  implementation(logback.classic)
  implementation(mu)
  implementation(swaggerUi)

  // TEST
  testImplementation(project(":testing"))

  testImplementation(http4k.clientOkhttp)
  testImplementation(http4k.clientWebSocket)
}

application {
  mainClass.set("net.lab0.skyscrapers.server.ApplicationKt")
}

tasks {
  test {
    useJUnitPlatform()
  }
}
