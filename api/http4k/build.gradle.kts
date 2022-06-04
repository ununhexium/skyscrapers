import dependencies.Dependencies.http4k

plugins {
    kotlin("jvm")
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":api:dto"))

    api(http4k.core)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
