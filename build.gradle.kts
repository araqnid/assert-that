plugins {
    kotlin("multiplatform") version "1.3.71"
    `maven-publish`
}

group = "org.araqnid.kotlin.assert-that"
version = "0.0.0"

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

    sourceSets["commonMain"].dependencies {
        implementation(kotlin("stdlib-common"))
        implementation(kotlin("test-common"))
        compileOnly(kotlin("test-annotations-common"))
    }

    sourceSets["commonTest"].dependencies {
    }

    sourceSets["jvmMain"].dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("test-junit"))
    }

    sourceSets["jvmTest"].dependencies {
    }

    sourceSets["jsMain"].dependencies {
        implementation(kotlin("stdlib-js"))
        implementation(kotlin("test-js"))
    }

    sourceSets["jsTest"].dependencies {
    }
}

dependencies {
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
