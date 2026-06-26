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
    compileOnly("com.pinterest.ktlint:ktlint-rule-engine-core:1.8.0")
    compileOnly("com.pinterest.ktlint:ktlint-cli-ruleset-core:1.8.0")

    testImplementation("com.pinterest.ktlint:ktlint-rule-engine-core:1.8.0")
    testImplementation("com.pinterest.ktlint:ktlint-test:1.8.0")
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.5.16")
}

tasks.test {
    useJUnitPlatform()
}
