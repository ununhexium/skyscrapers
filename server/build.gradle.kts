import dependencies.Dependencies.http4k

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
  implementation(project(":logic"))

  http4k()

  // TEST

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")

  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

application {
  mainClass.set("net.lab0.skyscrapers.server.ApplicationKt")
}

tasks {
  test {
    useJUnitPlatform()
  }
}