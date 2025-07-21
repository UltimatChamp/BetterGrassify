plugins {
    id("fabric-loom") version "1.11-SNAPSHOT"
    id("me.modmuss50.mod-publish-plugin") version "0.8.4"
}

var isSnapshot = false
var mcVer = project.property("minecraft_version") as String
if (mcVer.contains("-") || mcVer.contains("w")) {
    isSnapshot = true
    mcVer = mcVer.replace("-", "")
}

version = "${project.property("mod_version")}+fabric.$mcVer"
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("mod_name") as String)
}

repositories {
    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven") }
        filter { includeGroup("maven.modrinth") }
    }
    maven("https://maven.parchmentmc.org")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${project.property("minecraft_version")}:${project.property("parchment_version")}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fapi_version")}")

    modApi("me.shedaniel.cloth:cloth-config-fabric:${project.property("clothconfig_version")}")
    modApi("com.terraformersmc:modmenu:${project.property("modmenu_version")}")
    modImplementation("maven.modrinth:sodium:${project.property("sodium_version")}")

    // Compat
    modCompileOnly("maven.modrinth:wilder-wild:${project.property("wilderwild_version")}")
    modCompileOnly("maven.modrinth:frozenlib:${project.property("frozenlib_version")}")
}

tasks.processResources {
    val replaceProperties = mapOf(
        "minecraft_range" to project.property("mc_range"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.version,
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description")
    )
    replaceProperties.forEach { (key, value) -> inputs.property(key, value) }

    filesMatching("fabric.mod.json") {
        expand(replaceProperties)
    }
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

publishMods {
    val modVersion = project.property("mod_version") as String
    type = when {
        modVersion.contains("alpha") -> ALPHA
        modVersion.contains("beta") || modVersion.contains("rc") || isSnapshot -> BETA
        else -> STABLE
    }

    changelog.set("# ${project.version}\n${rootProject.file("CHANGELOG.md").readText()}")
    file.set(tasks.remapJar.get().archiveFile)
    displayName.set("BetterGrassify ${project.version}")
    modLoaders.addAll("fabric", "quilt")

    modrinth {
        projectId.set(project.property("modrinthId") as String)
        accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))

        minecraftVersions.addAll("1.21.6", "1.21.7", "1.21.8")

        requires("fabric-api")
        optional("yacl")

        // Discord
        announcementTitle.set("Download from Modrinth")
    }

    curseforge {
        projectId.set(project.property("curseforgeId") as String)
        accessToken.set(providers.environmentVariable("CURSEFORGE_API_KEY"))

        minecraftVersions.addAll("1.21.6", "1.21.7", "1.21.8")
        javaVersions.add(JavaVersion.VERSION_21)

        clientRequired.set(true)
        serverRequired.set(false)

        requires("fabric-api")
        optional("yacl")

        // Discord
        announcementTitle.set("Download from CurseForge")
        projectSlug.set("bettergrassify")
    }

    github {
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
        repository.set("UltimatChamp/BetterGrassify")
        commitish.set("main")

        // Discord
        announcementTitle.set("Download from GitHub")
    }

    discord {
        webhookUrl.set(providers.environmentVariable("DISCORD_WEBHOOK"))
        username.set("BetterGrassify Releases")
        avatarUrl.set("https://cdn.modrinth.com/data/m5T5xmUy/c67c1f900e8344e462bb5c21fb512579f3b0be46.png")

        style {
            look.set("MODERN")
            thumbnailUrl.set("https://cdn.modrinth.com/data/m5T5xmUy/images/3eaeda7d3cc1cec804e5176d9a21d8eb0f7ab8b6.png")
        }
    }
}
