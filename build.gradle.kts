import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.springframework.boot") version "2.7.0-RC1" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("jvm") version "1.6.20" apply false
    kotlin("plugin.spring") version "1.6.20" apply false
    kotlin("plugin.jpa") version "1.6.20" apply false
    idea
}

allprojects {
    group = "com.trivialepic.mockexchange"
    version = "0.0.1"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()

        testLogging {
            events("passed", "skipped", "failed") //, "standardOut", "standardError"

            showExceptions = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showCauses = true
            showStackTraces = true

            showStandardStreams = false
        }
    }

}

subprojects {
    apply {
        plugin("io.spring.dependency-management")
        plugin("idea")
    }

    idea {
        module {
            isDownloadSources = true
            isDownloadJavadoc = true
        }
    }
}
