plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.kikugie.dev/snapshots")
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("me.modmuss50:mod-publish-plugin:1.1.0")
    implementation("dev.kikugie:fletching-table:0.1.0-alpha.22")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.2.21-2.0.5")
    implementation("dev.kikugie:stonecutter:0.8.3")
}
