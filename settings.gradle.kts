pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8.2"
}

rootProject.name = "ClickVillagers"

// 注释掉 mod，只保留 paper
// include("paper", "mod")
include("paper")

// 注释整个 stonecutter 区块，不再构建多版本模组
/*
stonecutter {
    kotlinController = true
    create("mod") {
        fun version(version: String, vararg loaders: String) {
            loaders.forEach {
                this.version("$version-$it", version)
                    .buildscript = "build.$it.gradle.kts"
            }
        }
        version("1.21.1", "fabric", "neoforge")
        version("1.21.4", "fabric", "neoforge")
        version("1.21.5", "fabric", "neoforge")
        version("1.21.8", "fabric", "neoforge")
        version("1.21.10", "fabric", "neoforge")
        version("1.21.11", "fabric", "neoforge")
        version("26.1", "fabric+noremap")
        vcsVersion = "26.1-fabric+noremap"
    }
}
*/