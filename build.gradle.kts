import com.vanniktech.maven.publish.SonatypeHost
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.JavadocJar
import org.jetbrains.dokka.gradle.DokkaTaskPartial

group = "nl.w8mr.parsek"
version = scmVersion.version

repositories {
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.dokka)
//    alias(libs.plugins.dokkaJavadoc)
    alias(libs.plugins.publish)
    alias(libs.plugins.axionRelease)

}

val multiplatformId = libs.plugins.kotlinMultiplatform.get().pluginId
val dokkaId = libs.plugins.dokka.get().pluginId
//val dokkaJavadocId = libs.plugins.dokkaJavadoc.get().pluginId
val publishId = libs.plugins.publish.get().pluginId

scmVersion {
    repository {
        // Only push tags, don't try to create GitHub releases
        pushTagsOnly = true
    }
}


subprojects {

    apply(plugin = publishId)
    apply(plugin = multiplatformId)
    apply(plugin = dokkaId)
    //apply(plugin = dokkaJavadocId)

    dokka {
        moduleName.set("Parsek")
        dokkaPublications.html {
            suppressInheritedMembers.set(true)
            failOnWarning.set(true)
        }


    }

    mavenPublishing {
        configure(KotlinMultiplatform(
            javadocJar = JavadocJar.Dokka("dokkaHtml"),
            sourcesJar = true,
            androidVariantsToPublish = emptyList<String>(),
        ))
        publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

        coordinates("nl.w8mr.parsek", "core")

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
