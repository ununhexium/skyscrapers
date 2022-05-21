import dependencies.Dependencies.http4k

plugins {
    kotlin("jvm")

    kotlin("plugin.serialization") version Versions.kotlin
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":api:structure"))

    implementation(http4k("format-kotlinx-serialization"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
