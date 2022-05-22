import dependencies.Dependencies.arrow
import dependencies.Dependencies.http4k

plugins {
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  api(project(":api:dto"))

  api(arrow)
  implementation(http4k.core)
  implementation(http4k.clientOkhttp)
  implementation(http4k.formatKotlinxSerialization)

  // TEST
  testImplementation(project(":server"))
  testImplementation(project(":testing"))
  testImplementation(http4k.serverUndertow)
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}
