pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.6"
}

stonecutter {
    create(rootProject) {
        versions("1.21.1", "1.21.8")
        vcsVersion = "1.21.8"
    }
}

rootProject.name = "BetterGrassify"
