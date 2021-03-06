import dependencies.Dependencies.http4k
import dependencies.Dependencies.mu
import dependencies.Dependencies.spring

plugins {
    kotlin("jvm")
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("plugin.spring") version "1.6.21"
}

group = "net.lab0.skyscrapers"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":ai"))
    implementation(project(":api:structure"))
    implementation(project(":client:http"))
    implementation(project(":server"))
    implementation(project(":engine"))
    implementation(project(":testing"))

    implementation(http4k.clientOkhttp)
    implementation(http4k.serverUndertow)

    implementation(mu)

    implementation("org.springframework.boot:spring-boot-starter")
    implementation(spring.shell)

    // TEST

    testImplementation(project(":engine"))
    testImplementation(project(":server"))

    testImplementation("com.ninja-squad:springmockk:3.1.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

springBoot {
    mainClass.set("net.lab0.skyscrapers.client.shell.spring.ApplicationKt")
}