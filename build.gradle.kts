buildscript {
    repositories {
        mavenCentral()
        maven("https://files.minecraftforge.net/maven")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
    dependencies {
        classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "2.1-SNAPSHOT")
        classpath(group = "gradle.plugin.com.matthewprenger", name = "CurseGradle", version = "1.0.10")
        classpath(group = "org.spongepowered", name = "mixingradle", version = "0.6-SNAPSHOT")
        classpath(group = "com.github.jengelman.gradle.plugins", name = "shadow", version = "1.2.3")
    }
}

apply {
    plugin("net.minecraftforge.gradle.forge")
    plugin("org.spongepowered.mixin")
    plugin("com.github.johnrengelman.shadow")
}

plugins {
    java
    `java-library`
    //id("org.jetbrains.kotlin.jvm") version "1.7.0-RC2"
    id("net.minecraftforge.gradle.forge") version "2.0.2"
}

repositories {
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

group = "io.karma"
version = "1.0.0-release.1"

minecraft {
    version = "1.8.9-11.15.1.1722"
    runDir = "run"
    mappings = "stable_22"
    makeObfSourceJar = true
}

dependencies {
    implementation(group = "org.jetbrains", name = "annotations", version = "23.0.0")
    implementation(group = "org.spongepowered", name = "mixin", version = "0.6.4-SNAPSHOT") {
        exclude(module = "launchwrapper")
    }
    implementation(group = "it.unimi.dsi", name = "fastutil", version = "8.5.8")
}

tasks {
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["MixinConfigs"] = "mixins.glfont.json"
        attributes["tweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
        attributes["TweakOrder"] = 0
        attributes["FMLCorePluginContainsFMLMod"] = "io.karma.glfont.injection.MixinLoader"
    }
}
