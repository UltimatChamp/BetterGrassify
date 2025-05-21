pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.architectury.dev")
        maven("https://maven.fabricmc.net")
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.6"
}

stonecutter {
    create(rootProject) {
        vers("1.21.5-fabric", "1.21.5")

        vers("1.21.4-fabric", "1.21.4")

        vers("1.21.3-fabric", "1.21.3")

        vers("1.21.1-fabric", "1.21.1")
        vers("1.21.1-neoforge", "1.21.1")

        vcsVersion = "1.21.5-fabric"
    }
}

rootProject.name = "BetterGrassify"
