import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
}

android {
    namespace = "io.stape.sgtm"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.stape.sgtm"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        android.buildFeatures.buildConfig = true

        //Make sure you've added the file stape.properties to the root of this (app) module
        val stapeProperties = Properties().apply {
            load(file("stape.properties").inputStream())
        }
        buildConfigField("String", "STAPE_SERVER_HOST", "\"${stapeProperties.getProperty("server.host")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
//    implementation(project(":sdk"))

    implementation("io.stape:android-sgtm:1.0-SNAPSHOT")

    implementation(libs.firebase.analitics)

    implementation(libs.okhttp3.logging)

    implementation(libs.google.material)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
}
