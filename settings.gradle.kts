pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8.3"
}

stonecutter {
    create(rootProject) {
        fun register(vararg versions: String) = versions
            .forEach {
                if (stonecutter.eval(it, ">1.21.11"))
                    version(it).buildscript = "build_noremap.gradle.kts"
                else version(it).buildscript = "build.gradle.kts"
            }

        register("1.21.1", "1.21.10", "1.21.11", "26.1.2")
        vcsVersion = "26.1.2"
    }
}

rootProject.name = "BetterGrassify"
