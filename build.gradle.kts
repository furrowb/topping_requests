import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.3"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	id("org.flywaydb.flyway") version "9.8.1"
	id("nu.studer.jooq") version "8.1"
}

group = "com.bfurrow"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.xerial:sqlite-jdbc:3.30.1")
	implementation("org.jooq:jooq:3.17.8")
	jooqGenerator("org.xerial:sqlite-jdbc:3.30.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val jdbcUrl = "jdbc:sqlite:${projectDir.absolutePath}/pizzeria.db"

flyway {
	url = jdbcUrl
}

flyway.cleanDisabled = false
tasks.withType<KotlinCompile> {
	dependsOn("flywayMigrate")
}

jooq {
	configurations {
		create("main") {
			jooqConfiguration.apply {
				jdbc.apply {
					driver = "org.sqlite.JDBC"
					url = jdbcUrl
				}
				generator.apply {
					target.apply {
						packageName = "com.bfurrow.toppings"
					}
				}
			}
		}
	}
}

java.sourceSets["main"].java {
	srcDir("generated-src/jooq/main")
}