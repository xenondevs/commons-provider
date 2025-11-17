plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.kotlin)
    alias(libs.plugins.dokka)
}

group = "xyz.xenondevs.commons"
version = "2.0.0-alpha.8"

repositories {
    mavenCentral()
    maven("https://repo.xenondevs.xyz/releases/")
}

dependencies {
    api(libs.kotlin.stdlib)
    api(libs.commons.tuple)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.platformLauncher)
    testImplementation(libs.kotlin.test.junit)
}

java {
    withSourcesJar()
}

tasks {
    test {
        useJUnitPlatform()
        maxParallelForks = Runtime.getRuntime().availableProcessors()
    }
}

publishing {
    repositories {
        maven {
            credentials {
                name = "xenondevs"
                url = uri { "https://repo.xenondevs.xyz/releases/" }
                credentials(PasswordCredentials::class)
            }
        }
    }
    
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}