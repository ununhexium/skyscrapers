import dependencies.Dependencies.http4k

plugins {
    kotlin("jvm")

    kotlin("plugin.serialization") version Versions.kotlin
}

group = "net.lab0.skyscrapers"

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
