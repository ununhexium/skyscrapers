import dependencies.Dependencies.clikt
import dependencies.Dependencies.hoplite
import dependencies.Dependencies.http4k
import dependencies.Dependencies.koin
import dependencies.Dependencies.kotlinxSerializationJson
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.serialization") version Versions.kotlin
}

group = "net.lab0.skyscrapers"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":client:http"))

    implementation(clikt)
    implementation(hoplite.core)
    implementation(hoplite.json)
    implementation(kotlinxSerializationJson)

    implementation(http4k.clientOkhttp)

    implementation(koin.core)

    // TEST
    testImplementation(project(":engine"))
    testImplementation(project(":server"))
    testImplementation(project(":testing"))

    testImplementation(http4k.serverUndertow)

    testImplementation(koin.junit4)
    testImplementation(koin.junit5)
    testImplementation(koin.test)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = Versions.java
}

application {
    mainClass.set("net.lab0.skyscrapers.client.clikt.ApplicationKt")
}
