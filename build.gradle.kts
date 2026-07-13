plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
	jacoco
	id("org.sonarqube") version "7.3.1.8318"
}

group = "io.hexlet"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("net.datafaker:datafaker:2.0.1")
	implementation("org.instancio:instancio-junit:3.3.0")

	runtimeOnly("com.h2database:h2")
	compileOnly ("org.projectlombok:lombok:1.18.38")

	annotationProcessor ("org.projectlombok:lombok:1.18.38")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testCompileOnly ("org.projectlombok:lombok:1.18.38")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor ("org.projectlombok:lombok:1.18.38")
	testImplementation(platform("org.junit:junit-bom:5.12.0"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.12.0")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
// Понадобится когда мы начнем работать с аутентификацией
	testImplementation("org.springframework.security:spring-security-test")
}
tasks.test {
	useJUnitPlatform()
}
jacoco {
	toolVersion = "0.8.13"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}

tasks.check {
	dependsOn(tasks.jacocoTestReport)
}
sonar {
	properties {
		property("sonar.projectKey", "NikitaOguz_hexlet-spring-blog")
		property("sonar.organization", "nikitoguzkov")
		property("sonar.host.url", "https://sonarcloud.io")

		property(
			"sonar.coverage.jacoco.xmlReportPaths",
			"build/reports/jacoco/test/jacocoTestReport.xml"
		)

		property(
			"sonar.junit.reportPaths",
			"build/test-results/test"
		)
	}
}