@file:Suppress("UnstableApiUsage")

import me.cortex.voxy.gradle.prop
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile

extra["loaderName"] = "fabric"
extra["loaderDisplayName"] = "Fabric"
extra["archiveTaskName"] = "remapJar"
extra["sourceJavaDir"] = "src/fabric/java"
extra["additionalSourceJavaDirs"] = listOf(
    "versions/${prop("deps.minecraft", "minecraft_version")}/src/java",
    "versions/${prop("deps.minecraft", "minecraft_version")}-fabric/src/fabric/java"
)

plugins {
    id("fabric-loom") version "1.16.2"
}

apply(from = rootProject.file("build.common.gradle.kts"))

repositories {
    maven(rootProject.file("local-maven"))
    mavenCentral()
}

tasks.named<Jar>("sourcesJar").configure {
    dependsOn("stonecutterPrepare", "stonecutterGenerate")
}

afterEvaluate {
    extensions.findByType(SourceSetContainer::class.java)?.let { ssc ->
        val versionedFabricRes = rootProject.file("versions/${minecraftVersion}-fabric/src/fabric/resources")
        ssc.named("main") {
            java.setSrcDirs(
                listOf(
                    // Stonecutter-processed shared core (src/main/java)
                    file("build/generated/stonecutter/main/java"),
                    // loader-shared sources
                    rootProject.file("src/fabric/java"),
                    // per-Minecraft-version sources shared across loaders
                    rootProject.file("versions/${minecraftVersion}/src/java"),
                    // per-variant fabric sources
                    rootProject.file("versions/${minecraftVersion}-fabric/src/fabric/java")
                )
            )
            java.exclude(
                "**/mixin/flashback/**",
                "**/mixin/nvidium/**",
                "**/mixin/chunky/**"
            )
            // Stonecutter only chisels Java into its generated tree; resources are not
            // redirected automatically. Point the resource roots at the chiseled mirror so
            // version/loader-conditional files (the mixin configs, accesswidener) are valid
            // for this variant instead of the raw templated src/main/resources.
            resources.setSrcDirs(
                listOfNotNull(
                    file("build/generated/stonecutter/main/resources"),
                    rootProject.file("src/main/generated"),
                    versionedFabricRes.takeIf { it.exists() }
                )
            )
        }
    }
    // Ensure stonecutter generation runs before compilation/resource processing for this variant
    tasks.matching { it.name == "compileJava" || it.name == "processResources" }.configureEach {
        dependsOn("stonecutterPrepare", "stonecutterGenerate")
    }
}


val modId: String by extra
val minecraftVersion: String by extra
val jedisVersion: String by extra
val rocksdbVersion: String by extra
val commonsPoolVersion: String by extra
val lz4Version: String by extra
val xzVersion: String by extra
val sqliteJdbcVersion: String by extra

val lwjglVersion = "3.3.3"

// Force LWJGL to the 1.20.1-compatible version for 1.20.1 variant projects.
if (project.name.startsWith("1.20.1")) {
    configurations.configureEach {
        resolutionStrategy {
            // Force specific LWJGL artifacts
            force("org.lwjgl:lwjgl:3.3.3", "org.lwjgl:lwjgl-lmdb:3.3.3", "org.lwjgl:lwjgl-zstd:3.3.3", "org.lwjgl:lwjgl-bom:3.3.3")
            // Ensure any org.lwjgl dependency uses 3.3.1
            eachDependency {
                if (requested.group == "org.lwjgl") {
                    useVersion("3.3.3")
                }
            }
        }
    }
}


loom {
    accessWidenerPath = sc.process(rootProject.file("src/main/resources/voxy.accesswidener"), "build/processed.accesswidener")
}

dependencies {
//    annotationProcessor("net.fabricmc:sponge-mixin:0.17.2+mixin.0.8.7")
    annotationProcessor(files(rootProject.file("local-deps/mixinextras-0.5.4.jar")))
    compileOnly(files(rootProject.file("local-deps/mixinextras-0.5.4.jar")))
    compileOnly("net.fabricmc:sponge-mixin:0.17.2+mixin.0.8.7")

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.layered {
        officialMojangMappings()
        providers.gradleProperty("deps.parchment").orNull?.let { parchment("org.parchmentmc.data:parchment-$it@zip") }
    })

    modImplementation(files(rootProject.file("local-deps/fabric-loader-0.19.3.jar")))

    val fabricApiVersion = prop("deps.fabric-api", "fabric_api_version")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    val sodiumFabricVer = prop("deps.sodium")
    val useModrinth = prop("deps.sodium.modrinth").toBoolean()
    if (useModrinth) {
        modImplementation("maven.modrinth:sodium:$sodiumFabricVer")
    } else {
        modImplementation(files(rootProject.file("local-deps/sodium-fabric-0.8.12+mc1.21.1.jar")))
    }

    val lithiumFabricVer = prop("deps.lithium")
    modImplementation(files(rootProject.file("local-deps/lithium-fabric-0.15.4+mc1.21.1.jar")))

    modCompileOnly(files(rootProject.file("local-deps/modmenu-11.0.4.jar")))

    modCompileOnly(files(rootProject.file("local-deps/iris-fabric-1.8.14-beta.1+mc1.21.1.jar")))

    modCompileOnly(files(rootProject.file("local-deps/sodium-extra-fabric-0.9.1+mc1.21.1.jar")))

    implementation(include("redis.clients:jedis:5.1.0")!!)
    implementation(include("org.rocksdb:rocksdbjni:10.2.1")!!)
    implementation(include("org.apache.commons:commons-pool2:2.12.0")!!)
    implementation(include("org.lz4:lz4-java:1.8.0")!!)
    implementation(include("org.tukaani:xz:1.10")!!)
    implementation(include("org.lwjgl:lwjgl-lmdb:3.3.3")!!)
    implementation(include("org.lwjgl:lwjgl-zstd:3.3.3")!!)
    implementation(include("org.lwjgl:lwjgl-lmdb:3.3.3:natives-windows")!!)
    implementation(include("org.lwjgl:lwjgl-zstd:3.3.3:natives-windows")!!)
    implementation(include("org.lwjgl:lwjgl-lmdb:3.3.3:natives-linux")!!)
    implementation(include("org.lwjgl:lwjgl-zstd:3.3.3:natives-linux")!!)
}

fabricApi {
    configureDataGeneration() {
        outputDirectory = file("$rootDir/src/main/generated")
        client = true
    }
}

// The mixin configs (client/common.voxy.mixins.json) live in src/main/resources and are
// chiseled by Stonecutter into build/generated/stonecutter/main/resources, which is the
// main resource source dir. processResources copies them as-is; Fabric's mixin loader
// reads the chiseled output (which retains `//?` markers as comments) without issue.
