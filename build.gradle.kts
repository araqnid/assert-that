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
    val bintrayUser = (project.properties["bintray.user"] ?: "").toString()
    val bintrayKey = (project.properties["bintray.apiKey"] ?: "").toString()
    repositories {
        maven(url = "https://api.bintray.com/maven/araqnid/maven/assert-that/;publish=1") {
            name = "bintray"
            credentials {
                username = bintrayUser
                password = bintrayKey
            }
        }
    }
}
