plugins {
    id("java")
}

group = "net.lab0.skyscrapers"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api:structure"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
