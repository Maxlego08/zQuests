plugins {
    `maven-publish`
}

rootProject.extra.properties["sha"]?.let { sha ->
    version = sha
}

dependencies {
    compileOnly(files("../libs/zMenu-API-1.1.0.0.jar"))
}

tasks {
    shadowJar {
        relocate("com.tcoded.folialib", "fr.maxlego08.quests.libs.folia")

        destinationDirectory.set(rootProject.extra["apiFolder"] as File)
    }

    build {
        dependsOn(shadowJar)
    }
}

publishing {
    repositories {
        maven {
            name = "groupezSnapshots"
            url = uri("https://repo.groupez.dev/snapshots")
            credentials {
                username = findProperty("${name}Username") as String? ?: System.getenv("MAVEN_USERNAME")
                password = findProperty("${name}Password") as String? ?: System.getenv("MAVEN_PASSWORD")
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        register<MavenPublication>("groupezSnapshots") {
            pom {
                groupId = project.group as String?
                name = "${rootProject.name}-${project.name}"
                artifactId = name.get().lowercase()
                version = project.version as String?

                scm {
                    connection = "scm:git:git://github.com/MaxLego08/${rootProject.name}.git"
                    developerConnection = "scm:git:ssh://github.com/MaxLego08/${rootProject.name}.git"
                    url = "https://github.com/MaxLego08/${rootProject.name}/"
                }
            }
            artifact(tasks.shadowJar)
            artifact(tasks.javadocJar)
            artifact(tasks.sourcesJar)
        }
    }
}