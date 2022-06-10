import dependencies.Dependencies.clikt
import dependencies.Dependencies.jline
import dependencies.Dependencies.jlineBuiltins

plugins {
  application
  java
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"

repositories {
  mavenCentral()
}

dependencies {

  implementation(project(":api:structure"))
  implementation(project(":client:clikt"))
  implementation(project(":engine"))

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
