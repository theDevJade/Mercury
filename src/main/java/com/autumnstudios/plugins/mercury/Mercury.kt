package com.autumnstudios.plugins.mercury

import com.autumnstudios.mercury.utils.Horizon
import com.autumnstudios.plugins.mercury.npc.NPCDataStorage
import org.bukkit.plugin.java.JavaPlugin
import org.checkerframework.checker.nullness.qual.NonNull
import org.checkerframework.framework.qual.DefaultQualifier

@DefaultQualifier(NonNull::class)
class Mercury : JavaPlugin() {

  companion object {
    lateinit var instance: Mercury;

    fun getInstance() : Mercury {
      return Mercury.instance
    }
  }

  lateinit var dataStorageNPC: NPCDataStorage;


  fun register() {
    instance = this;
    dataStorageNPC = NPCDataStorage(this)
  }
    override fun onEnable() {


      logger.info("Starting up Mercury (Library)")
      val versionList: List<String> = Horizon.quickList("1.20.1", "1.20")

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
    }
    override fun onDisable() {}


}
