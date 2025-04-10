plugins {
    kotlin("jvm") version "2.0.21"
}

group = "nl.w8mr"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("nl.w8mr:parsek:0.0.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}