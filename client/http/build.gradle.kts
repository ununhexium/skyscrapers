import dependencies.Dependencies.arrow
import dependencies.Dependencies.http4k

plugins {
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"

repositories {
  mavenCentral()
}

dependencies {
  api(project(":api:dto"))
  api(project(":api:http4k"))

  api(arrow)
  api(http4k.core)
  implementation(http4k.clientOkhttp)
  implementation(http4k.clientWebSocket)
  implementation(http4k.formatKotlinxSerialization)

  implementation(
    group = "org.http4k",
    name = "http4k-core",
    version = "4.27.0.0"
  )
  implementation(
    group = "org.http4k",
    name = "http4k-server-jetty",
    version = "4.27.0.0"
  )
  implementation(
    group = "org.http4k",
    name = "http4k-client-websocket",
    version = "4.27.0.0"
  )
  implementation(
    group = "org.http4k",
    name = "http4k-format-jackson",
    version = "4.27.0.0"
  )

  // TEST
  testImplementation(project(":engine"))
  testImplementation(project(":server"))
  testImplementation(project(":testing"))
  testImplementation(http4k.serverUndertow)
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}
