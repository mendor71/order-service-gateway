import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    val kotlinVersion = "1.6.10"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    application
}

group = "com.mendor71"
version = "0.1.0"

repositories {
    maven(url = "https://packages.confluent.io/maven")
    mavenCentral()
    mavenLocal()
}

application {
    mainClass.set("MainKt")
}

val coroutinesVersion = "1.6.0"

dependencies {
    implementation(group = "com.mendor71", name = "order-service-lib", version = "0.4.0")
    implementation(group = "org.springframework.kafka", name = "spring-kafka")

    implementation(group = "org.springframework.boot", name = "spring-boot-starter-webflux")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-actuator")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-test")
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin")
    implementation(group = "com.fasterxml.jackson.datatype", name = "jackson-datatype-jsr310")

    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = coroutinesVersion)
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core-jvm", version = coroutinesVersion)
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-reactor", version = coroutinesVersion)
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-reactive", version = coroutinesVersion)
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-slf4j", version = coroutinesVersion)

    testImplementation(kotlin("test"))
    testImplementation(group = "io.mockk", name = "mockk", version = "1.12.2")
    testImplementation(group = "io.kotest", name = "kotest-runner-junit5", version = "5.1.0")
    testImplementation(group = "io.kotest", name = "kotest-property", version = "5.1.0")
    testImplementation(group = "io.kotest.extensions", name = "kotest-extensions-spring", version = "1.1.0")
    testImplementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version = coroutinesVersion)
    testImplementation(group = "org.springframework.kafka", name = "spring-kafka-test")
    testImplementation(group = "org.testcontainers", name = "kafka", version = "1.15.3")

    testImplementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.1")
    testImplementation(group = "com.squareup.okhttp3", name = "mockwebserver", version = "4.9.1")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
