import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import dependencies.Dependencies.http4k
import dependencies.TestDependencies.junit5Api
import dependencies.TestDependencies.junit5Engine
import dependencies.TestDependencies.assertJ


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

  // TEST

  testImplementation(assertJ)
  testImplementation(junit5Api)
  testRuntimeOnly(junit5Engine)
}

tasks {
  getByName<Test>("test") {
    useJUnitPlatform()
  }

  withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
  }
}
