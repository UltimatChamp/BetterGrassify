plugins {
    id("project-base")
    id("net.fabricmc.fabric-loom")
}

dependencies {
    minecraft("com.mojang:minecraft:${stonecutter.current.project}")

    implementation("net.fabricmc:fabric-loader:${project.property("mc.loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${project.property("deps.fapi_version")}")

    api("me.shedaniel.cloth:cloth-config-fabric:${project.property("deps.clothconfig_version")}") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    api(fletchingTable.modrinth("modmenu", stonecutter.current.project + " + 26.2-rc-2", "fabric"))

    compileOnly(fletchingTable.modrinth("sodium", stonecutter.current.project + " + 26.1.2", "fabric"))

    // Compat
    compileOnly(fletchingTable.modrinth("wilder-wild", stonecutter.current.project + " + 26.1.2", "fabric"))
    compileOnly(fletchingTable.modrinth("frozenlib", stonecutter.current.project + " + 26.1.2", "fabric"))
}

loom {
    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "../../run"
    }
}

publishMods {
    file.set(tasks.jar.flatMap { it.archiveFile })
    additionalFiles.from(tasks.sourcesJar.flatMap { it.archiveFile })
}
