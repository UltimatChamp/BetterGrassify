plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.6-fabric"

stonecutter.tasks {
    order(named("publishMods"))
}
