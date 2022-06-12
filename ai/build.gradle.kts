import dependencies.Dependencies.assertJ
import dependencies.Dependencies.junit5Api
import dependencies.Dependencies.junit5Engine


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
