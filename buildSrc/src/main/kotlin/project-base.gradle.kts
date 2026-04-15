import dev.kikugie.stonecutter.build.StonecutterBuildExtension

plugins {
    id("java-library")
    id("me.modmuss50.mod-publish-plugin")

    kotlin("jvm")
    id("com.google.devtools.ksp")
    id("dev.kikugie.fletching-table")
}

val stonecutter = project.extensions.getByType(StonecutterBuildExtension::class.java)

var isSnapshot = false
var mcVer = stonecutter.current.project
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
    mavenCentral()
    exclusiveContent {
        forRepository { maven("https://api.modrinth.com/maven") }
        filter { includeGroup("maven.modrinth") }
    }
    maven("https://maven.shedaniel.me/")
}

project.afterEvaluate {
    stonecutter.let { sc ->
        sc.replacements {
            string {
                direction = sc.eval(sc.current.version, ">1.21.10")
                replace("ResourceLocation", "Identifier")
            }
            string {
                direction = sc.eval(sc.current.version, ">1.21.11")
                replace("GuiGraphics", "GuiGraphicsExtractor")
            }
        }
    }
}

tasks.named("processResources") {
    dependsOn(":${stonecutter.current.project}:stonecutterGenerate")
}

tasks.processResources {
    var breaksRange: String = if (project.hasProperty("deps.breaks_range"))
                                  project.property("deps.breaks_range") as String
                              else ""
    val replaceProperties = mapOf(
        "minecraft_range" to project.property("mc.range"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.version,
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description"),
        "breaks_range" to breaksRange
    )
    replaceProperties.forEach { (key, value) -> inputs.property(key, value) }

    filesMatching("fabric.mod.json") {
        expand(replaceProperties)
    }
}

fletchingTable {
    mixins.create("main") {
        mixin("default", "bettergrass.mixins.json")
    }
}

val javaVer = if (stonecutter.eval(stonecutter.current.version, ">1.21.11"))
                  JavaVersion.VERSION_25
              else JavaVersion.VERSION_21

java {
    withSourcesJar()

    sourceCompatibility = javaVer
    targetCompatibility = javaVer
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

    displayName.set("BetterGrassify ${project.version}")
    modLoaders.addAll("fabric", "quilt")

    modrinth {
        projectId.set(project.property("modrinthId") as String)
        accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))

        when (stonecutter.current.project) {
            "26.1.2" -> minecraftVersions.addAll("26.1", "26.1.1", "26.1.2")
            "1.21.11" -> minecraftVersions.add("1.21.11")
            "1.21.10" -> minecraftVersions.addAll("1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10")
            "1.21.1" -> minecraftVersions.addAll("1.21", "1.21.1")
        }

        requires("fabric-api")
        optional("cloth-config")

        // Discord
        announcementTitle.set("Download from Modrinth")
    }

    curseforge {
        projectId.set(project.property("curseforgeId") as String)
        accessToken.set(providers.environmentVariable("CURSEFORGE_API_KEY"))

        when (stonecutter.current.project) {
            "26.1.2" -> minecraftVersions.addAll("26.1", "26.1.1", "26.1.2")
            "1.21.11" -> minecraftVersions.add("1.21.11")
            "1.21.10" -> minecraftVersions.addAll("1.21.6", "1.21.7", "1.21.8", "1.21.9", "1.21.10")
            "1.21.1" -> minecraftVersions.addAll("1.21", "1.21.1")
        }

        javaVersions.add(javaVer)

        clientRequired.set(true)
        serverRequired.set(false)

        requires("fabric-api")
        optional("cloth-config")

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