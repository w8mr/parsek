//import com.vanniktech.maven.publish.SonatypeHost
//import com.vanniktech.maven.publish.KotlinMultiplatform
//import com.vanniktech.maven.publish.JavadocJar

group = "nl.w8mr.parsek"
version = "0.0.3-SNAPSHOT"


plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.dokka)
    `maven-publish`
    signing
}

val multiplatformId = libs.plugins.kotlinMultiplatform.get().pluginId
val dokkaId = libs.plugins.dokka.get().pluginId

subprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin = "signing")
    apply(plugin = "maven-publish")
    apply(plugin = multiplatformId)
    apply(plugin = dokkaId)

    signing {
        setRequired { System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey=") != null }
        useInMemoryPgpKeys(
            System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey="),
            System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyPassword")
        )
        sign(publishing.publications)
    }

    val isSnapshot = version.toString().endsWith("SNAPSHOT")

    publishing {
        publications {

            repositories {
                maven {

                    name = "oss"
                    url = when {
                        isSnapshot -> uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                        else -> uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    }
                    credentials {
                            username = System.getenv("SONATYPE_USERNAME")
                            password = System.getenv("SONATYPE_PASSWORD")
                    }
                }
            }
            withType<MavenPublication> {

                // Stub javadoc.jar artifact
                artifact(tasks.register("${name}JavadocJar", Jar::class) {
                    archiveClassifier.set("javadoc")
                    archiveAppendix.set(this@withType.name)
                })

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
}



//    mavenPublishing {
//        coordinates(groupId = "nl.w8mr.parsek", version="0.0.2")
//        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
//        configure(
//            KotlinMultiplatform(
//                javadocJar = JavadocJar.Dokka("dokkaHtml"),
//                sourcesJar = true,
//            )
//        )
//
//        pom {
//            name.set("Parsek")
//            description.set("The parser combinator for (and written in) Kotlin")
//            inceptionYear.set("2024")
//            url.set("https://github.com/w8mr/parsek")
//
//            licenses {
//                license {
//                    name.set("MIT License")
//                    url.set("https://opensource.org/license/mit")
//                    distribution.set("https://opensource.org/license/mit")
//                }
//            }
//
//            developers {
//                developer {
//                    id.set("w8mr")
//                    name.set("Elmar Wachtmeester")
//                    url.set("https://github.com/w8mr")
//                }
//            }
//
//            scm {
//                url.set("https://github.com/w8mr/parsek/")
//                connection.set("scm:git:git://github.com/w8mr/parsek.git")
//                developerConnection.set("scm:git:ssh://git@github.com:w8mr/parsek.git")
//            }
//        }
//
//        signAllPublications()
//    }
