plugins {
}

group = "net.lab0.skyscrapers"

repositories {
    mavenCentral()
}

dependencies {
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}