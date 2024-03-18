import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
    `maven-publish`

}

group = "nl.w8mr"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
<<<<<<< HEAD
=======
    implementation(project(":parsek"))
    implementation(project(":kasmine"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
>>>>>>> cbfd594 (feat: Added function declaration)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}