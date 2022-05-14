import dependencies.Dependencies
import dependencies.clikt

plugins {
  application
  java
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {

  implementation(project(":logic"))
//  implementation(Dependencies.clikt)

  clikt()

  implementation(Dependencies.jline)
  implementation(Dependencies.jlineBuiltins)

  // TEST

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

application {
  mainClass.set("net.lab0.skyscrapers.client.shell.jline3.ApplicationKt")
}
