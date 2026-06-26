plugins {
    kotlin("jvm") version "2.1.0"
}

group = "net.minecraft_community"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly("com.pinterest.ktlint:ktlint-cli-ruleset-core:1.8.0")
}
