package com.autumnstudios.plugins.mercury.startup

import com.autumnstudios.plugins.mercury.Mercury
import com.autumnstudios.plugins.mercury.api.commands.TestCommands
import com.autumnstudios.plugins.mercury.api.effects.PermPotionManager
import com.autumnstudios.plugins.mercury.api.npc.NPCDataStorage
import com.autumnstudios.plugins.mercury.api.schedulers.GlobalSchedulersData
import com.autumnstudios.plugins.mercury.modules.murky.MurkyCommand
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.PluginManager
import revxrsal.commands.bukkit.BukkitCommandHandler

class StartupManager {

  val pluginManager: PluginManager = Bukkit.getServer().pluginManager
  private val server: Server = Bukkit.getServer()
  private val logger = server.logger
  private val instance = Mercury.getMercury()

  init {
      startup()
  }

  private fun startup() {
    if (!checkVersions()) {
      return
    }
    if (!dependencyCheck()) {
      return
    }

    registerVariables()
    registerCommands()

    sucessfulLoad()
  }

  private fun registerCommands() {
    val cmHandler: BukkitCommandHandler = BukkitCommandHandler.create(instance)

    cmHandler.register(TestCommands())
    cmHandler.register(MurkyCommand())
  }

  private fun registerVariables() {
    instance.dataStorageNPC = NPCDataStorage(instance)
    instance.globalSchedulersData = GlobalSchedulersData()
    instance.permPotionsManager = PermPotionManager()
  }

  private fun dependencyCheck() : Boolean {
    var missingProtocolLib: Boolean = false
    var missingPacketEvents: Boolean = false
    var missingFAWE: Boolean = false

    var missingDepend: Boolean = false

    if (pluginManager.getPlugin("ProtocolLib") == null) {
      missingProtocolLib = true
      missingDepend = true
    }
    if (pluginManager.getPlugin("packetevents") == null) {
      missingPacketEvents = true
      missingDepend = true
    }


    if (missingDepend) {
      logger.severe("#######################")
      logger.severe("")
      logger.severe("MERCURY DEPEND ISSUES")
      logger.severe("YOU MAY BE MISSING SOME OF THE DEPENDENCIES WE REQUIRE")
      logger.severe("We require: ProtocolLib, PacketEvents")
      logger.severe("")
      logger.severe("We will now proceed to attempt to download these automatically")
      logger.severe("")
      logger.severe("#######################")
      logger.severe("")
      logger.severe("|#######################|")
      logger.severe("THIS WILL RESTART YOUR SERVER, BE WARNED")
      logger.severe("IF YOU WANT TO DISABLE THIS, YOU CAN SET \"auto-download\" TO FALSE IN THE CONFIG")
      logger.severe("|#######################|")
      DependencyDownloader().run(missingProtocolLib, missingPacketEvents)
      return false
    } else {
      return true
    }


  }

  private fun checkVersions() : Boolean {
    return if (!(server.version.contains("1.20.1"))) {
      logger.severe("=====================================")
      logger.severe("")
      logger.severe("Mercury")
      logger.severe("Currently, due to NMS compatibility")
      logger.severe("Your running ${server.version}")
      logger.severe("We only allow versions: 1.20.1")
      logger.severe("")
      logger.severe("=====================================")
      server.pluginManager.disablePlugin(instance)
      false
    } else {
      true
    }

  }

  fun sucessfulLoad() {
    logger.severe("=====================================")
    logger.severe("")
    logger.severe("Mercury")
    logger.severe("We have liftoff!")
    logger.severe("Mercury enabled ${server.version}")
    logger.severe("")
    logger.severe("Made by Jade <3")
    logger.severe("")
    logger.severe("=====================================")

  }
}
