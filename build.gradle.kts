plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"

    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
}

group = "com.aideesigns"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // Core
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // Security (needed soon anyway)
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Email
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // Monitoring (VERY SMART early addition)
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Swagger (massive DX improvement)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Dev
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}


//
//flyway {
//    url = "jdbc:postgresql://localhost:5432/aideesigns"
//    user = "aideesigns_user"
//    password = "devpassword"
//}

tasks.withType<Test> {
    useJUnitPlatform()
}
