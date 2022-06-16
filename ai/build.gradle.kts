import dependencies.Dependencies.assertJ
import dependencies.Dependencies.janino
import dependencies.Dependencies.junit5Api
import dependencies.Dependencies.junit5Engine
import dependencies.Dependencies.log4j
import dependencies.Dependencies.logback
import dependencies.Dependencies.mu


plugins {
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"

repositories {
  mavenCentral()
}

dependencies {

  implementation(project(":api:structure"))
  implementation(project(":engine"))

  implementation(mu)

  // TEST

  testImplementation(assertJ)
  testImplementation(janino.janino)
  testImplementation(janino.commonsCompiler)
  testImplementation(junit5Api)
  testImplementation(logback.classic)
  testRuntimeOnly(junit5Engine)
}

tasks {
  getByName<Test>("test") {
    useJUnitPlatform()
  }
}
