plugins {
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}


dependencies {
  implementation(project(":api:structure"))

  // TEST
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}
