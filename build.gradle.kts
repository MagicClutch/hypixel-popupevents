import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.4.0"
    id("net.fabricmc.fabric-loom-remap") version "1.17-SNAPSHOT"
    id("maven-publish")
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val targetJavaVersion = 21
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}



repositories {
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") {
        name = "DevAuth"
    }
    maven("https://maven.isxander.dev/releases") {
        name = "Xander Maven"
    }
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
    }
    maven("https://maven.terraformersmc.com/releases") {
        name = "TerraformersMC"
    }
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    add("modImplementation", "net.fabricmc:fabric-loader:${project.property("loader_version")}")
    add("modImplementation", "net.fabricmc:fabric-language-kotlin:${project.property("kotlin_loader_version")}")

    add("modImplementation", "net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    add("modImplementation", "net.fabricmc.fabric-api:fabric-command-api-v2:2.4.7+6b42a6003e")
    add("modImplementation", "net.fabricmc.fabric-api:fabric-message-api-v1:6.1.12+4ebb5c083e")
    add("modImplementation", "net.fabricmc.fabric-api:fabric-rendering-v1:16.2.9+7edacff13e")
    add("modImplementation", "net.fabricmc.fabric-api:fabric-key-binding-api-v1:1.1.7+4fc5413f3e")
    add("modImplementation", "net.fabricmc.fabric-api:fabric-lifecycle-events-v1:2.6.15+4ebb5c083e")
    add("modImplementation", "maven.modrinth:yacl:${project.property("yacl_version")}")
    include("maven.modrinth:yacl:${project.property("yacl_version")}")
    add("modImplementation", "maven.modrinth:modmenu:${project.property("modmenu_version")}")
    add("modRuntimeOnly", "me.djtheredstoner:DevAuth-fabric:${project.property("devauth_version")}")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version").toString(),
            "loader_version" to project.property("loader_version").toString(),
            "kotlin_loader_version" to project.property("kotlin_loader_version").toString()
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

tasks.withType<JavaExec>().configureEach {
    javaLauncher.set(
        javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
        }
    )

    if (name == "runClient") {
        jvmArgs(
            "-Ddevauth.enabled=true",
            "-Ddevauth.account=${project.findProperty("devauth_account") ?: "main"}"
        )
    }
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}
