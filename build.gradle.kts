import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.6.20"
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

subprojects {

  apply(plugin = "org.jetbrains.kotlin.jvm")

  dependencies {

    // TEST

    testImplementation("org.assertj:assertj-core:3.22.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
  }
}

tasks {
  test {
    useJUnitPlatform()
  }

  withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
  }

  clean {
    val c = this
    subprojects.forEach { project ->
      project.tasks.findByName("clean")?.let { task ->
        c.dependsOn(task)
      }
    }
  }
}
