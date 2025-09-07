plugins {
    alias(libs.plugins.dokka)
    alias(libs.plugins.dokkaJavadoc)
}

dokka {
        moduleName.set("Parsek core")
        dokkaPublications.html {
            suppressInheritedMembers.set(true)
            failOnWarning.set(true)
        }
        dokkaPublications.javadoc {
            suppressInheritedMembers.set(true)
            failOnWarning.set(true)
        }


    }

tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

repositories {
    mavenCentral()
    mavenLocal()
}

group = parent?.group ?: group
version = parent?.version ?: version

kotlin {
    jvm {
        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(11))
            }
        }
    }
    js {
        browser()
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotest)
            }
        }
    }
}
