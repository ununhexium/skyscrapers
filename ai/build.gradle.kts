import dependencies.Dependencies.assertJ
import dependencies.Dependencies.junit5Api
import dependencies.Dependencies.junit5Engine


plugins {
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {

  implementation(project(":engine"))
  implementation(project(":api:structure"))

  // TEST

  testImplementation(assertJ)
  testImplementation(junit5Api)
  testRuntimeOnly(junit5Engine)
}

tasks {
  getByName<Test>("test") {
    useJUnitPlatform()
  }
}
