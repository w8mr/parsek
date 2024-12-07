import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm") version "1.9.25"
    application
    id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "nl.w8mr"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

mavenPublishing {
    coordinates(artifactId = "parsek")
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

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

        developers {
            developer {
                id.set("w8mr")
                name.set("Elmar Wachtmeester")
                url.set("https://github.com/w8mr")
            }
        }

        scm {
            url.set("https://github.com/w8mr/parsek/")
            connection.set("scm:git:git://github.com/w8mr/parsek.git")
            developerConnection.set("scm:git:ssh://git@github.com:w8mr/parsek.git")
        }
    }

    signAllPublications()
}
