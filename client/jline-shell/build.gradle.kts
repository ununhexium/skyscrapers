import dependencies.Dependencies.clikt
import dependencies.Dependencies.jline
import dependencies.Dependencies.jlineBuiltins

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

  implementation(project(":engine"))
  implementation(project(":client:clikt"))

  implementation(clikt)
  implementation(jline)
  implementation(jlineBuiltins)

  // TEST
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

application {
  mainClass.set("net.lab0.skyscrapers.client.shell.jline3.ApplicationKt")
}
