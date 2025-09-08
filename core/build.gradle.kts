plugins {
    alias(libs.plugins.dokka)
    kotlin("jupyter.api") version "${libs.versions.jupyter.get()}"

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
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlin-jupyter-api:\"${libs.versions.jupyter.get()}\"")
            }
        }
    }
}
