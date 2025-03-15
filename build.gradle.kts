import com.vanniktech.maven.publish.SonatypeHost
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.JavadocJar

group = "nl.w8mr.parsek"
version = "0.1.0"


plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
}

val multiplatformId = libs.plugins.kotlinMultiplatform.get().pluginId
val dokkaId = libs.plugins.dokka.get().pluginId
val publishId = libs.plugins.publish.get().pluginId

subprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin = publishId)
    apply(plugin = multiplatformId)
    apply(plugin = dokkaId)

//    signing {
//        setRequired { System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey=") != null }
//        useInMemoryPgpKeys(
//            System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey="),
//            System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyPassword")
//        )
//        sign(publishing.publications)
//    }

//    val isSnapshot = version.toString().endsWith("SNAPSHOT")


    mavenPublishing {
        configure(KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true,
            androidVariantsToPublish = emptyList<String>(),
        ))
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

        coordinates("nl.w8mr.parsek", "core", "0.1.0")

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
        signAllPublications()

    }

}

//    publishing {
//        publications {

//            repositories {
//                maven {
//
//                    name = "oss"
//                    url = when {
//                        isSnapshot -> uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
//                        else -> uri("https://central.sonatype.org/service/local/staging/deploy/maven2/")
//                    }
//                    credentials {
//                            username = System.getenv("ORG_GRADLE_PROJECT_mavenCentralUsername")
//                            password = System.getenv("ORG_GRADLE_PROJECT_mavenCentralPassword")
//                        }
//                }
//            }
//            withType<MavenPublication> {
//
//                // Stub javadoc.jar artifact
//                artifact(tasks.register("${name}JavadocJar", Jar::class) {
//                    archiveClassifier.set("javadoc")
//                    archiveAppendix.set(this@withType.name)
//                })
//
//        }
//    }
//}
//
//

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
