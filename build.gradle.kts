import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.6.20"
  id("org.jetbrains.kotlinx.kover") version "0.5.1"
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

/**
 * Runs this task and all the tasks with the same name in the subprojects
 */
fun Task.recursively() {
  val thisTask = this
  subprojects.forEach { project ->
    project.tasks.findByName(thisTask.name)?.let { task ->
      thisTask.dependsOn(task)
    }
  }
}

tasks {
  test {
    useJUnitPlatform()

    this.recursively()
  }

  withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
  }

  clean {
    recursively()
  }

//  TODO: reintroduce after https://github.com/Kotlin/kotlinx-kover/issues/179 is fixed.
//  koverMergedVerify {
//    rule {
//      bound {
//        minValue = 50
//      }
//    }
//  }
}
