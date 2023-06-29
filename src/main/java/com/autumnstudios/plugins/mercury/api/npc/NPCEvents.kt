package com.autumnstudios.plugins.mercury.api.npc

import com.autumnstudios.plugins.mercury.Mercury
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class NPCEvents : Listener {

  @EventHandler
  public fun onJoin(event: PlayerJoinEvent) {
    val storage: NPCDataStorage = Mercury.getMercury().dataStorageNPC

    for ((id, n) in storage.dataStorage.entries) {
      if (n.global) {
        n.show(event.player)
      }
    }
  }
}
