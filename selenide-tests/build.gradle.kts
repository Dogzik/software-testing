import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("io.qameta.allure") version "2.9.6"
}

group = "com.example"
version = "1.0-SNAPSHOT"
val junit5Version = "5.8.2"
val allureVersion = "2.17.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("com.codeborne:selenide:6.2.0")
    testImplementation("org.slf4j:slf4j-simple:1.7.32")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit5Version")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junit5Version")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit5Version")
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("allure.results.directory", "$projectDir/build/allure-results")
    systemProperty("selenide.headless", "true")
    systemProperty("selenide.baseUrl", System.getProperty("selenide.baseUrl", "http://localhost:3000"))
    systemProperty("selenide.driverManagerEnabled", "false")
    systemProperty("selenide.remote", System.getProperty("selenide.remote", "http://localhost:4444/wd/hub"))
    systemProperty("selenide.browser", System.getProperty("selenide.browser", "chrome"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
