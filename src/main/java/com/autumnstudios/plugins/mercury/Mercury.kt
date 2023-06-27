package com.autumnstudios.plugins.mercury

import com.autumnstudios.mercury.utils.Horizon
import com.autumnstudios.plugins.mercury.commands.TestCommands
import com.autumnstudios.plugins.mercury.npc.NPCDataStorage
import org.bukkit.plugin.java.JavaPlugin
import org.checkerframework.checker.nullness.qual.NonNull
import org.checkerframework.framework.qual.DefaultQualifier
import revxrsal.commands.bukkit.BukkitCommandHandler

@DefaultQualifier(NonNull::class)
class Mercury : JavaPlugin() {

  companion object {
    lateinit var instance: Mercury;

    fun getMercury() : Mercury {
      return Mercury.instance
    }
  }

  lateinit var dataStorageNPC: NPCDataStorage;


  fun register() {
    // Registering variables, and instance
    instance = this;
    dataStorageNPC = NPCDataStorage(this)
    // ##################
    registerCommands()
  }

  fun registerCommands() {
    val cmHandler: BukkitCommandHandler = BukkitCommandHandler.create(this)
    cmHandler.register(TestCommands())
  }
    override fun onEnable() {

      check()


      logger.info("Starting up Mercury (Library)")
      val versionList: List<String> = Horizon.quickList("1.20.1", "1.20")


    }
    override fun onDisable() {}

  fun check() {
    // Initial Check
    if (!(server.version.contains("1.20.1"))) {
      logger.severe("=====================================")
      logger.severe("")
      logger.severe("Mercury")
      logger.severe("Currently, due to NMS compatibility")
      logger.severe("Your running ${server.version}")
      logger.severe("We only allow versions: 1.20.1")
      logger.severe("")
      logger.severe("=====================================")
      server.pluginManager.disablePlugin(this)
      return
    } else {
      logger.severe("=====================================")
      logger.severe("")
      logger.severe("Mercury")
      logger.severe("We have liftoff!")
      logger.severe("Mercury enabled ${server.version}")
      logger.severe("")
      logger.severe("Made by Jade <3")
      logger.severe("")
      logger.severe("=====================================")
      register()
    }

    // Further Checks
    var prohibited = false;
    var error: String = ""

    var reqPlugins = "ProtocolLib"

    if (server.pluginManager.getPlugin("ProtocolLib") == null) {
      prohibited = true;
      error = "MISSING REQUIRED PLUGINS: ${reqPlugins}"
    }

    if (prohibited) {
      logger.severe("############################")
      logger.severe("")
      logger.severe(error)
      logger.severe("")
      logger.severe("############################")

      server.pluginManager.disablePlugin(this)
    } else {
      register()
    }
  }


}
