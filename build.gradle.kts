@file:Suppress("UnstableApiUsage")

plugins {
    id("project-base")
    id("net.fabricmc.fabric-loom-remap")
}

repositories {
    maven("https://maven.parchmentmc.org")
}

dependencies {
    minecraft("com.mojang:minecraft:${stonecutter.current.project}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${stonecutter.current.project}:${project.property("mc.parchment_version")}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${project.property("mc.loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("deps.fapi_version")}")

    modApi("me.shedaniel.cloth:cloth-config-fabric:${project.property("deps.clothconfig_version")}")
    modApi(fletchingTable.modrinth("mOgUt4GM", stonecutter.current.project, "fabric"))

    modImplementation(fletchingTable.modrinth("sodium", stonecutter.current.project, "fabric"))

    // Compat
    modCompileOnly(fletchingTable.modrinth("wilder-wild", stonecutter.current.project, "fabric"))
    modCompileOnly(fletchingTable.modrinth("frozenlib", stonecutter.current.project, "fabric"))
}

loom {
    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../run"
    }
}

publishMods {
    file.set(tasks.remapJar.flatMap { it.archiveFile })
    additionalFiles.from(tasks.remapSourcesJar.flatMap { it.archiveFile })
}
