package com.autumnstudios.plugins.mercury

import com.autumnstudios.plugins.mercury.api.utils.ColorUtil
import com.autumnstudios.plugins.mercury.api.commands.TestCommands
import com.autumnstudios.plugins.mercury.api.effects.PermPotionManager
import com.autumnstudios.plugins.mercury.api.npc.NPCDataStorage
import com.autumnstudios.plugins.mercury.api.schedulers.GlobalSchedulersData
import com.autumnstudios.plugins.mercury.modules.murky.MurkyCommand
import org.bukkit.Bukkit
import org.bukkit.entity.Player
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

    fun getGlobalScheduler() : GlobalSchedulersData {
      return Mercury.instance.globalSchedulersData
    }

    fun getPermPotionsManager() : PermPotionManager {
      return Mercury.instance.permPotionsManager
    }
  }

  lateinit var dataStorageNPC: NPCDataStorage;
  lateinit var globalSchedulersData: GlobalSchedulersData;
  lateinit var permPotionsManager: PermPotionManager



  private fun register() {
    // Registering variables, and instance
    instance = this;
    dataStorageNPC = NPCDataStorage(this)
    this.globalSchedulersData = GlobalSchedulersData()
    permPotionsManager = PermPotionManager()
    // ##################
    registerCommands()
  }

  private fun registerCommands() {
    val cmHandler: BukkitCommandHandler = BukkitCommandHandler.create(this)
    cmHandler.register(TestCommands())
    cmHandler.register(MurkyCommand())
  }
    override fun onEnable() {
      logger.info("Starting up Mercury (Library)")
      if (Bukkit.getOnlinePlayers().isNotEmpty()) {
        for (p: Player in Bukkit.getOnlinePlayers()) {
          p.kick(ColorUtil.getTextComponent("&c&lSERVER RELOAD - MERCURY"))
        }
      }
      check()






    }
    override fun onDisable() {}

  private fun check() {
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

    if (!server.pluginManager.isPluginEnabled("ProtocolLib")) {
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
