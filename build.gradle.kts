plugins {
    kotlin("multiplatform") version "1.4.30"
    `maven-publish`
}

group = "org.araqnid.kotlin.assert-that"
version = "0.1.1"

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
        if (isGithubUserAvailable(project)) {
            maven(url = "https://maven.pkg.github.com/araqnid/assert-that") {
                name = "github"
                credentials(githubUserCredentials(project))
            }
        }
    }
}
