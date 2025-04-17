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

    mavenPublishing {
        configure(KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true,
            androidVariantsToPublish = emptyList<String>(),
        ))
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

        coordinates("nl.w8mr.parsek", "core", "0.1.1")

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