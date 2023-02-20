import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.2"
	id("io.spring.dependency-management") version "1.1.0"
  id("jacoco")
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
  kotlin("plugin.jpa") version "1.7.22"
}

jacoco{
  toolVersion="0.8.8"
}

group = "com.salt.developerjokes.api"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
  testImplementation("org.springframework.security:spring-security-test")
  runtimeOnly("org.postgresql:postgresql")
  runtimeOnly("com.h2database:h2")
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
  developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.test {
  finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
  dependsOn(tasks.test)
}

tasks.withType<Test> {
	useJUnitPlatform()
  finalizedBy(tasks.jacocoTestReport)
}

tasks.withType<JacocoReport> {
  reports{
    xml.required.set(true)
    html.required.set(true)
  }

  afterEvaluate {
    classDirectories.setFrom(files(classDirectories.files.map {
      fileTree(it).apply {
        exclude( "**/DeveloperJokesApplication**")
      }
    }))
  }

}
