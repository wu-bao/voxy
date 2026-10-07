pluginManagement {
    repositories {
        maven(uri("local-maven")) { name = "BundledLocalDependencies" }
        mavenLocal()
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
        maven("https://maven.su5ed.dev/releases/") { name = "Sinytra" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.4"
}

stonecutter {
    // Configuration goes here
    create(rootProject) {
        fun match(version: String, vararg loaders: String) = loaders
            .forEach { version("$version-$it", version).buildscript = "build.$it.gradle.kts" }
        match("1.21.1", "fabric")
        vcsVersion = "1.21.1-fabric"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)

    repositories {
        maven(uri("local-maven")) { name = "BundledLocalDependencies" }
        mavenLocal()
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        mavenCentral()
        maven("https://api.modrinth.com/maven") { name = "Modrinth" }
        maven("https://maven.shedaniel.me/") { name = "Shedaniel" }
        maven("https://maven.terraformersmc.com/releases/") { name = "TerraformersMC" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
    }
}
