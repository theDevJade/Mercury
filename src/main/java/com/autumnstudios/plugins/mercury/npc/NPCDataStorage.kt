package com.autumnstudios.plugins.mercury.npc

import com.autumnstudios.mercury.nms.npc.NPC
import com.autumnstudios.plugins.mercury.Mercury
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player


class NPCDataStorage(main: Mercury) {

  public val dataStorage: HashMap<Int, NPC> = HashMap();
  private val instance: Mercury;

  var protcolLibrary: ProtocolManager

  init {
    protcolLibrary = ProtocolLibrary.getProtocolManager()
    instance = main;
    registerListeners()
  }

  private fun flatoutInit() {
    Bukkit.getServer().pluginManager.registerEvents(NPCEvents(), instance)
  }

  private fun registerListeners() {

    protcolLibrary.addPacketListener(object : PacketAdapter(instance, ListenerPriority.HIGHEST, PacketType.Play.Client.USE_ENTITY) {
      override fun onPacketReceiving(event: PacketEvent) {
        val entityID = event.packet.integers.read(0)

        clickEvent(entityID, event)
      }
    })

    flatoutInit()
  }

  fun clickEvent(entityID: Int, event: PacketEvent) {
    if (containsID(entityID)) {
      event.isCancelled = true;
      val npc: NPC = locateWithID(entityID)!!
      if (npc.npcExtensive != null) {
        npc.npcExtensive!!.onClick(ClickType.LEFT_CLICK, event.player)
      }

    }
  }

  fun add(entityID: Int, npc: NPC) {
    dataStorage.put(entityID, npc)
  }

  fun locateWithID(entityID: Int) : NPC? {
    return dataStorage.get(entityID)
  }

  fun containsID(entityID: Int) : Boolean {
    return dataStorage.contains(entityID)
  }

  fun remove(entityID: Int) {
    dataStorage.remove(entityID)
  }

  fun backTrackNPC(npc: NPC) : Int? {
    for ((id, n) in dataStorage.entries) {
      if (n == npc) return id;
    }

    return null;
  }

  fun deleteAll(confirm: Boolean) {
    for ((id, n) in dataStorage.entries) {
      n.removeAll()
    }
  }



}
