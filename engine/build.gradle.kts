plugins {
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":api:structure"))
  implementation(project(":rule"))

  // TEST
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}
