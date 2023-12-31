import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
  `java-library`
  `maven-publish`
  id("io.papermc.paperweight.userdev") version "1.5.5"
  id("xyz.jpenilla.run-paper") version "2.1.0" // Adds runServer and runMojangMappedServer tasks for testing

  // Shades and relocates dependencies into our plugin jar. See https://imperceptiblethoughts.com/shadow/introduction/
  id("com.github.johnrengelman.shadow") version "8.1.1"
  kotlin("jvm") version "1.9.0-RC"
}

group = "com.autumnstudios.plugins.mercury"
version = "2.0"
description = "Test plugin for paperweight-userdev"
// Latest is 1.20.1-R0.1-SNAPSHOT
// https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-1-16/
var minecraftVersion = "1.20.1-R0.1-SNAPSHOT"
var readableName = "1.20.1"

java {
  // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}



dependencies {

  paperweight.paperDevBundle(minecraftVersion)
  // paperweight.foliaDevBundle("1.20-R0.1-SNAPSHOT")
  // paperweight.devBundle("com.example.paperfork", "1.20-R0.1-SNAPSHOT")

  // Shadow will include the runtimeClasspath by default, which implementation adds to.
  // Dependencies you don't want to include go in the compileOnly configuration.
  // Make sure to relocate shaded dependencies!
  implementation("cloud.commandframework", "cloud-paper", "1.8.3")
  implementation(kotlin("stdlib-jdk8"))
  compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0")
  compileOnly("dev.simplix:protocolize-api:2.1.0")
  compileOnly("com.github.retrooper.packetevents:spigot:2.0.0-SNAPSHOT")
  compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.9")
  compileOnly("com.sk89q.worldedit:worldedit-core:7.2.0-SNAPSHOT")
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      groupId = "com.autumnstudios.plugins.mercury"
      artifactId = "mercury"
      from(components["java"])
      versionMapping {
        usage("java-api") {
          fromResolutionOf("runtimeClasspath")
        }
        usage("java-runtime") {
          fromResolutionResult()
        }
      }
      pom {
        name.set("Mercury")
        description.set("A concise description of my library")
        url.set("http://www.example.com/library")

        developers {
          developer {
            id.set("jade")
            name.set("Jade")
            email.set("john.doe@example.com")
          }
        }

      }
    }
  }


}




task("buildLibrary") {
  dependsOn("assemble", "publish")
  doLast {
    println("Built and published to local maven library")
  }
}

tasks {
  // Configure reobfJar to run when invoking the build task
  assemble {
    dependsOn(reobfJar)
  }



  publish {
    publishToMavenLocal
  }

  compileKotlin {
    kotlinOptions.javaParameters = true
  }

  compileJava {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything


    // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
    // See https://openjdk.java.net/jeps/247 for more information.
    options.release.set(17)
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
  }
  processResources {
    filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    val props = mapOf(
      "name" to project.name,
      "version" to project.version,
      "description" to project.description,
      "apiVersion" to "1.20"
    )
    inputs.properties(props)
    filesMatching("plugin.yml") {
      expand(props)
    }
  }

  reobfJar {
    outputJar.set(layout.buildDirectory.file("Mercury-mc-${readableName}-version-${version}.jar"))
  }

  shadowJar {
    // helper function to relocate a package into our package
    fun reloc(pkg: String) = relocate(pkg, "com.autumnstudios.plugins.mercury.dependency.$pkg")

    // relocate cloud and it's transitive dependencies
    reloc("cloud.commandframework")
    reloc("io.leangen.geantyref")
  }

}
repositories {
  maven(url = "https://jitpack.io")
  mavenCentral()
  maven("https://repo.dmulloy2.net/repository/public/")
  maven("https://repo.simplix.dev/repository/simplixsoft-public/")
  maven("https://mvn.exceptionflug.de/repository/exceptionflug-public/")
  maven("https://repo.codemc.io/repository/maven-snapshots/")
  maven("https://maven.enginehub.org/repo/")
}
kotlin {
  jvmToolchain(17)
}
