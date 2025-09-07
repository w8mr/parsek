group = "nl.w8mr.parsek"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.dokkaJavadoc)
    id("maven-publish")
    id("signing")
    alias(libs.plugins.axionRelease)
}

val multiplatformId = libs.plugins.kotlinMultiplatform.get().pluginId

scmVersion {
    repository {
        pushTagsOnly = true
    }
}

version = scmVersion.version

dokka {
    moduleName.set("Parsek")
    dokkaPublications.html {
        suppressInheritedMembers.set(true)
        failOnWarning.set(true)
    }
    dokkaPublications.javadoc {
        suppressInheritedMembers.set(true)
        failOnWarning.set(true)
    }
}

subprojects {
    project.version = rootProject.version

    apply(plugin = multiplatformId)
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components.findByName("kotlin") ?: components["java"])
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
        repositories {
            maven {
                name = "MavenCentral"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = System.getenv("OSSRH_USERNAME") ?: ""
                    password = System.getenv("OSSRH_PASSWORD") ?: ""
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
