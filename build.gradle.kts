plugins {
    id("java")
    id("java-base")
    id("java-library")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}


group = "de.timesnake"
version = "4.0.0"
var projectId = 35

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://git.timesnake.de/api/v4/groups/7/-/packages/maven")
        name = "timesnake"
        credentials(PasswordCredentials::class)
    }
}

val pluginImplementation: Configuration by configurations.creating
val pluginFile = layout.buildDirectory.file("libs/${project.name}-${project.version}-plugin.jar")
val pluginArtifact = artifacts.add("pluginImplementation", pluginFile.get().asFile) {
    builtBy("pluginJar")
}

dependencies {
    pluginImplementation("org.mariadb.jdbc:mariadb-java-client:3.0.6")

    api("de.timesnake:basic-proxy:3.+")

    implementation("org.apache.logging.log4j:log4j-api:2.22.1")
    implementation("org.apache.logging.log4j:log4j-core:2.22.1")

    implementation("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
}

configurations.all {
    resolutionStrategy.dependencySubstitution.all {
        requested.let {
            if (it is ModuleComponentSelector && it.group == "de.timesnake") {
                val targetProject = findProject(":${it.module}")
                if (targetProject != null) {
                    useTarget(targetProject)
                }
            }
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://git.timesnake.de/api/v4/projects/$projectId/packages/maven")
            name = "timesnake"
            credentials(PasswordCredentials::class)
        }
    }

    publications {
        create<MavenPublication>("maven") {
            artifacts {
                from(components["java"])
                artifact(pluginArtifact)
            }
        }
    }
}

tasks.register<Jar>("pluginJar") {
    from(pluginImplementation.map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
    archiveClassifier = "plugin"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    dependsOn("shadowJar", "assemble")
}

tasks.register<Copy>("exportPluginJar") {
    from(pluginFile)
    into(findProperty("timesnakePluginDir") ?: "")
    dependsOn("pluginJar")
}

tasks.withType<PublishToMavenRepository> {
    dependsOn("shadowJar", "pluginJar")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release = 21
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("plugin.yml") {
            expand(mapOf(Pair("version", project.version)))
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
}