import org.gradle.util.internal.GUtil.loadProperties
import java.net.URI

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
    signing
}

android {
    namespace = "io.stape.sgtm"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

private val properties = loadProperties(project.rootDir.resolve("signing/sign.properties"))

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.stape"
            artifactId = "android-sgtm"
            version = "1.0-SNAPSHOT"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("Android Stape SDK")
                url.set("https://github.com/stape-io/stape-sgtm-android")
                description.set(
                    "Stape SDK is an event based client for the Stape API. It allows " +
                            "to track and send events from your mobile app to the Stape backend."
                )
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://github.com/stape-io/stape-sgtm-android/blob/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("stape")
                        name.set("Stape Developer")
                        email.set("dev@stape.net")
                    }
                }
                scm {
                    connection.set("scm:git@github.com:stape-io/stape-sgtm-android.git")
                    url.set("https://github.com/stape-io/stape-sgtm-android")
                }
            }
        }
    }
    repositories {
        mavenLocal()
        maven {
            val publication = publications["release"] as MavenPublication
            val publicationVersion = publication.version.toString()
            // select appropriate repository based on version
            val repoName = if (publicationVersion.endsWith("SNAPSHOT")) "snapshot" else "release"

            name = "MavenCentral"
            url = URI.create(properties.getProperty("repository.$repoName.url"))

            credentials {
                username = properties.getProperty("repository.username")
                password = properties.getProperty("repository.password")
            }
        }
    }
}

signing {
    val signingSecretKey = properties.getProperty("signing.secretKey")
    val signingPassword = properties.getProperty("signing.password")

    useInMemoryPgpKeys(signingSecretKey, signingPassword)
    sign(publishing.publications["release"])
}

dependencies {
    implementation(libs.gson)
    implementation(libs.okhttp3.client)
    implementation(libs.firebase.analitics)
}
