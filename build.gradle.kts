plugins {
    `java-library`
    id("com.gradleup.shadow") version "9.0.0-beta11"
}

group = "fr.maxlego08.quests"
version = "1.0.0.0"

extra.set("targetFolder", file("target/"))
extra.set("apiFolder", file("target-api/"))
extra.set("classifier", System.getProperty("archive.classifier"))
extra.set("sha", System.getProperty("github.sha"))

allprojects {

    apply(plugin = "java-library")
    apply(plugin = "com.gradleup.shadow")

    group = "fr.maxlego08.quests"
    version = rootProject.version

    repositories {
        mavenLocal()
        mavenCentral()

        maven(url = "https://jitpack.io")
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven(url = "https://repo.papermc.io/repository/maven-public/")
        maven(url = "https://oss.sonatype.org/content/groups/public/")
        maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven(url = "https://repo.bg-software.com/repository/api/")
        maven(url = "https://repo.groupez.dev/releases")
        maven(url = "https://repo.groupez.dev/snapshots")
        maven(url = "https://repo.tcoded.com/releases")
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    tasks.javadoc {
        options.encoding = "UTF-8"
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")
        compileOnly("me.clip:placeholderapi:2.11.6")
        compileOnly("dev.krakenied:blocktracker:1.0.6")
        compileOnly("com.bgsoftware:WildStackerAPI:2024.3")
        compileOnly("com.bgsoftware:SuperiorSkyblockAPI:2024.4")

        compileOnly("com.mojang:authlib:3.11.50")

        compileOnly(files("libs/zJobs-1.0.0.jar"))
        compileOnly(files("libs/zShop-3.3.0.jar"))
        compileOnly(files("libs/zEssentials-1.0.2.6.jar"))
        compileOnly("fr.maxlego08.menu:zmenu-api:1.1.0.2")

        implementation("com.tcoded:FoliaLib:0.5.1")
        implementation("fr.maxlego08.sarah:sarah:1.18")
    }

}

dependencies {
    api(projects.api)
    // api(projects.hooks)
}

tasks {
    shadowJar {
        relocate("com.tcoded.folialib", "fr.maxlego08.quests.libs.folia")
        relocate("fr.maxlego08.sarah", "fr.maxlego08.quests.libs.sarah")

        rootProject.extra.properties["sha"]?.let { sha ->
            archiveClassifier.set("${rootProject.extra.properties["classifier"]}-${sha}")
        } ?: run {
            archiveClassifier.set(rootProject.extra.properties["classifier"] as String?)
        }
        destinationDirectory.set(rootProject.extra["targetFolder"] as File)
    }

    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.release = 21
    }

    processResources {
        from("resources")
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
}