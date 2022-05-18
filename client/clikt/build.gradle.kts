import dependencies.Dependencies.clikt

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
    implementation(clikt)

    // TEST
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
