plugins {
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
  repositories {
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    }
  }
}

dependencies {

  implementation("com.sparkjava:spark-kotlin:1.0.0-alpha")

  val kotlinxHtmlVersion = "0.7.5"
  implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:${kotlinxHtmlVersion}")
  implementation("org.jetbrains.kotlinx:kotlinx-html:${kotlinxHtmlVersion}")

  // TEST
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}