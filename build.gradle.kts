plugins {
	java
	id("org.springframework.boot") version "3.0.4"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.sonarqube") version "4.0.0.2929"
	jacoco
}

group = "com.b2"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2022.0.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation ("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.1.RELEASE")
	implementation ("org.springframework.security:spring-security-test")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("org.postgresql:postgresql")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	implementation("com.google.guava:guava:31.1-jre")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
	classDirectories.setFrom(files(classDirectories.files.map {
		fileTree(it) { exclude("**/*Application**") }
	}))
	dependsOn(tasks.test) // tests are required to run before generating the report
	reports {
		xml.required.set(true)
		csv.required.set(false)
		html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
	}
}

sonarqube {
	properties {
		property("sonar.projectKey", "reservationB2adpro")
		property("sonar.organization", "b2adpro")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}
