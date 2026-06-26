plugins {
    kotlin("jvm") version "2.1.0"
    `maven-publish`
}

group = "de.dweipert"
version = "1.0.0"

val ktlint: Configuration by configurations.creating

dependencies {
    ktlint("com.pinterest.ktlint:ktlint-cli:1.8.0")

    implementation("com.pinterest.ktlint:ktlint-cli-ruleset-core:1.8.0")
    implementation("com.pinterest.ktlint:ktlint-rule-engine-core:1.8.0")

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.0")
    testImplementation("org.junit.platform:junit-platform-launcher:6.1.0")
    testImplementation("org.slf4j:slf4j-simple:2.0.18")
    testImplementation("com.pinterest.ktlint:ktlint-test:1.8.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/licenses/MIT"
                        distribution = "repo"
                    }
                }
            }
        }
    }
}

val ktlintCheck by tasks.registering(JavaExec::class) {
    dependsOn(tasks.classes)
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = JavaLanguageVersion.of(21)
    }
    mainClass = "com.pinterest.ktlint.Main"
    classpath(ktlint, sourceSets.main.map { it.output })
    args("src/**/*.kt")
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}

tasks.check {
    dependsOn(ktlintCheck)
}
