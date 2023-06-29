package com.autumnstudios.plugins.mercury.startup

import com.autumnstudios.plugins.mercury.Mercury
import org.bukkit.Bukkit
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


class DependencyDownloader {

  fun run(missingProtocolLib: Boolean, missingPacketEvents: Boolean) {
    val logger = Bukkit.getLogger()


    if (missingProtocolLib) {
      generateMiddleMan("ProtocolLib")
    }
    if (missingPacketEvents) {
      generateMiddleMan("PacketEvents")
    }
  }

  fun download(depend: MercuryDepend) {
    val PROTOCOL_LIB = "https://github.com/dmulloy2/ProtocolLib/releases/download/5.0.0/ProtocolLib.jar"
    val PACKET_EVENTS = "https://ci.codemc.io/job/retrooper/job/packetevents/lastSuccessfulBuild/artifact/spigot/build/libs/packetevents-spigot-2.0.0-SNAPSHOT.jar"

    when (depend) {
      MercuryDepend.PROTOCOLLIB -> {
        urlDown(PROTOCOL_LIB, "ProtocolLib")
      }
      MercuryDepend.PACKETEVENTS -> {
        urlDown(PACKET_EVENTS, "PacketEvents")
      }

      else -> {}
    }

  }

  fun urlDown(url: String, name: String) {
    val `in` = URL(url).openStream()
    val uriString = Mercury.getMercury().dataFolder.parentFile.toString()
    val uri = URI(uriString + "${name}.jar")
    Files.copy(`in`, Paths.get(uri), StandardCopyOption.REPLACE_EXISTING)
  }

  fun generateMiddleMan(missing: String) {
    val logger = Bukkit.getLogger()

    logger.severe("################")
    logger.severe("MISSING ${missing}, NOW ATTEMPTING TO DOWNLOAD")
    logger.severe("################")

  }
}
