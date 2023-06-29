package com.autumnstudios.plugins.mercury

import com.autumnstudios.plugins.mercury.api.utils.ColorUtil
import com.autumnstudios.plugins.mercury.api.commands.TestCommands
import com.autumnstudios.plugins.mercury.api.effects.PermPotionManager
import com.autumnstudios.plugins.mercury.api.npc.NPCDataStorage
import com.autumnstudios.plugins.mercury.api.schedulers.GlobalSchedulersData
import com.autumnstudios.plugins.mercury.modules.murky.MurkyCommand
import com.autumnstudios.plugins.mercury.startup.StartupManager
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



  private fun importantRegister() {
    // Registering variables, and instance
    instance = this;

  }
  override fun onEnable() {
    importantRegister()
    logger.info("Starting up Mercury (Library)")

    val manager: StartupManager = StartupManager()

  }
  override fun onDisable() {}




}
