import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.20"
    java
}

repositories {
    mavenCentral()
}

val starsectorDir = "/home/cpai/Games/Games-hdd/starsector"
val modsDir = "${starsectorDir}/mods"

dependencies {
    testImplementation(kotlin("test"))
    implementation(fileTree(starsectorDir) {
        this.include("*.jar", "starfarer.api.zip")
    })
    implementation(files("${modsDir}/LazyLib/jars/LazyLib.jar"))
    implementation(files("${modsDir}/LazyLib/jars/LazyLib-Kotlin.jar"))
    implementation(files("${modsDir}/MagicLib/jars/MagicLib.jar"))
}

tasks {
    named<Jar>("jar") {
        destinationDirectory.set(file("$rootDir/out/GuaranteeRareItems/jars"))
        archiveFileName.set("gri.jar")
    }
}
