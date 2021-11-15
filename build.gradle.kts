import java.net.URI

plugins {
    kotlin("multiplatform") version "1.6.0"
    `maven-publish`
    signing
}

group = "org.araqnid.kotlin.assert-that"
version = "0.1.1"

description = "Kotlin multiplatform test assertion library"

repositories {
    mavenCentral()
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

tasks {
    withType<Jar>().configureEach {
        manifest {
            attributes["Implementation-Title"] = project.description ?: project.name
            attributes["Implementation-Version"] = project.version
        }
    }
}

val javadocJar = tasks.register("javadocJar", Jar::class.java) {
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set(project.name)
                description.set(project.description)
                licenses {
                    license {
                        name.set("Apache")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                url.set("https://github.com/araqnid/assert-that")
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/araqnid/assert-that/issues")
                }
                scm {
                    connection.set("https://github.com/araqnid/assert-that.git")
                    url.set("https://github.com/araqnid/assert-that")
                }
                developers {
                    developer {
                        name.set("Steven Haslam")
                        email.set("araqnid@gmail.com")
                    }
                }
            }
        }
    }

    repositories {
        val sonatypeUser: String? by project
        if (sonatypeUser != null) {
            maven {
                name = "OSSRH"
                url = URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                val sonatypePassword: String by project
                credentials {
                    username = sonatypeUser
                    password = sonatypePassword
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
