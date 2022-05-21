import dependencies.Dependencies.http4k
import dependencies.Dependencies.arrow

plugins {
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":api:dto"))
  implementation(project(":api:structure"))

  implementation(arrow)
  implementation(http4k.core)
  implementation(http4k.clientOkhttp)
  implementation(http4k.formatKotlinxSerialization)

  // TEST
  testImplementation(project(":server"))
  testImplementation(http4k.serverUndertow)
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}
