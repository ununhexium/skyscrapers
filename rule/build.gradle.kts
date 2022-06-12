plugins {
    kotlin("jvm")
}

group = "net.lab0.skyscrapers"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api:structure"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
