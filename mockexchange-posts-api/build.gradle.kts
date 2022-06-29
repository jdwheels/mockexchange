plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring") apply true
    kotlin("plugin.jpa")
    kotlin("kapt")
    java
    jacoco
}

ext {
    set("testcontainersVersion", "1.17.1")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${ext["testcontainersVersion"]}")
    }
}

dependencies {
    implementation(project(":mockexchange-objects"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-hateoas") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation("org.springframework.boot:spring-boot-starter-data-redis")
//    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
//    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    compileOnly("io.projectreactor:reactor-tools")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.postgresql:r2dbc-postgresql")
//    implementation("io.r2dbc:r2dbc-postgresql")
    implementation("org.apache.commons:commons-pool2")

    runtimeOnly("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.springframework.boot:spring-boot-starter-jdbc")

    compileOnly("org.springframework.boot:spring-boot-devtools")

    //    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.projectreactor:reactor-test")

    testImplementation("io.mockk:mockk")
    testImplementation("com.ninja-squad:springmockk:3.1.1")

    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:r2dbc")
    testImplementation("org.testcontainers:junit-jupiter")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}
