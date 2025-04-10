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
