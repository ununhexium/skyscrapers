plugins {
  application
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  mavenLocal() // for jline 3.21.1 snpashot
}

dependencies {

  val jline = "3.21.1-SNAPSHOT"
  implementation("org.jline:jline:$jline")
  implementation("org.jline:jline-builtins:$jline")

  // TEST

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

application {
  mainClass.set("net.lab0.skyscrapers.client.shell.jline3.Game")
}