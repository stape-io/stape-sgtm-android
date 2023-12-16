pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
//      Enable this if you want to use the published snapshot version of the SDK
//        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

rootProject.name = "stape-sgtm-android"
include(":app")
include(":sdk")
