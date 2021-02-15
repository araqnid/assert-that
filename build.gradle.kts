plugins {
    kotlin("multiplatform") version "1.4.30"
    `maven-publish`
}

group = "org.araqnid.kotlin.assert-that"
version = "0.1.0"

repositories {
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    jvm { }
    js {
        nodejs { }
        useCommonJs()
    }
}

dependencies {
    commonMainImplementation(kotlin("stdlib-common"))
    commonMainImplementation(kotlin("test-common"))
    commonMainCompileOnly(kotlin("test-annotations-common"))
    "jvmMainImplementation"(kotlin("stdlib-jdk8"))
    "jvmMainImplementation"(kotlin("test-junit"))
    "jsMainImplementation"(kotlin("stdlib-js"))
    "jsMainImplementation"(kotlin("test-js"))
}

publishing {
    repositories {
        maven(url = "https://maven.pkg.github.com/araqnid") {
            name = "github"
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
