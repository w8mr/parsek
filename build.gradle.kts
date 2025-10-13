group = "nl.w8mr.parsek"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.dokka)
    id("maven-publish")
    id("signing")
    alias(libs.plugins.axionRelease)

    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"

}

val multiplatformId = libs.plugins.kotlinMultiplatform.get().pluginId
val dokkaId = libs.plugins.dokka.get().pluginId

scmVersion {
    repository {
        pushTagsOnly = true
    }
}

publishing {
    repositories {
        maven {
            name = "Sonatype"
            url = uri(
                if (version.toString().endsWith("SNAPSHOT"))
                    "https://central.sonatype.com/repository/maven-snapshots/"
                else
                    "https://ossrh-staging-api.central.sonatype.com/service/local/"
            )
            credentials {
                username = System.getenv("ORG_GRADLE_PROJECT_sonatypeUsername")
                password = System.getenv("ORG_GRADLE_PROJECT_sonatypePassword")
            }
        }
    }
}

nexusPublishing {
    repositories {
        // see https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/#configuration
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username = System.getenv("ORG_GRADLE_PROJECT_sonatypeUsername")
            password = System.getenv("ORG_GRADLE_PROJECT_sonatypePassword")
        }
    }
}

version = scmVersion.version

subprojects {
    project.version = rootProject.version

    apply(plugin = multiplatformId)
    // Only apply the standard dokka plugin, not dokka-javadoc
    apply(plugin = dokkaId)
    apply(plugin = "maven-publish")
    apply(plugin = "signing")


    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["kotlin"]) // Use only the kotlin component
                groupId = "nl.w8mr.parsek"
                version = project.version.toString() // Use Axion plugin version
                pom {
                    name.set("Parsek")
                    description.set("The parser combinator for (and written in) Kotlin")
                    inceptionYear.set("2024")
                    url.set("https://github.com/w8mr/parsek")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/license/mit")
                            distribution.set("https://opensource.org/license/mit")
                        }
                    }
                    issueManagement {
                        system.set("Github")
                        url.set("https://github.com/w8mr/parsek/issues")
                    }
                    developers {
                        developer {
                            id.set("w8mr")
                            name.set("Elmar Wachtmeester")
                            url.set("https://github.com/w8mr")
                        }
                    }
                    scm {
                        url.set("https://github.com/w8mr/parsek/")
                        connection.set("https://github.com/w8mr/parsek.git")
                        developerConnection.set("scm:git:ssh://git@github.com:w8mr/parsek.git")
                    }
                }
            }
        }

    }

    signing {
        val signingKey: String? = System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey")
        val signingPassword: String? = System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyPassword")
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["maven"])
    }
}
