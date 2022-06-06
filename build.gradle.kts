import dependencies.Dependencies.assertJ
import dependencies.Dependencies.http4k
import dependencies.Dependencies.junit5Api
import dependencies.Dependencies.junit5Engine
import dependencies.Dependencies.kotest
import dependencies.Dependencies.mockK
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

    testImplementation(assertJ)
    testImplementation(http4k.kotest)
    testImplementation(kotest.core)
    testImplementation(kotest.arrowJvm)

    testImplementation(mockK)

    testImplementation(junit5Api)
    testRuntimeOnly(junit5Engine)
  }

  tasks {
    withType<KotlinCompile> {
      kotlinOptions.jvmTarget = Versions.java
      kotlinOptions.freeCompilerArgs = listOf(
        "-progressive",
      )
    }
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

    recursively()
  }

  clean {
    recursively()
  }

  kover {
    // https://github.com/Kotlin/kotlinx-kover/issues/179#issuecomment-1137464869
    intellijEngineVersion.set("1.0.669")
    runAllTestsForProjectTask = true
  }

  koverMergedVerify {
    rule {
      name = "Minimal line coverage rate in percent"
      bound {
        minValue = 70
      }
    }
  }
}
