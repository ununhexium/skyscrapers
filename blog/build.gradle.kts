import dependencies.Dependencies.kotlinxHtml
import dependencies.Dependencies.kotlinxHtmlJvm

plugins {
  application
  kotlin("jvm")
}

group = "net.lab0.skyscrapers"

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

  implementation(kotlinxHtml)
  implementation(kotlinxHtmlJvm)


  // TEST
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

application {
  mainClass.set("net.lab0.skyscrapers.blog.MainKt")
}
