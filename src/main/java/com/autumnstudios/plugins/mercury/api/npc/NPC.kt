package com.autumnstudios.mercury.nms.npc

import com.autumnstudios.plugins.mercury.api.utils.ReflectionUtils
import com.autumnstudios.plugins.mercury.Mercury
import com.autumnstudios.plugins.mercury.api.npc.NPCExtensive
import com.autumnstudios.plugins.mercury.api.utils.ColorUtil
import com.autumnstudios.plugins.mercury.api.quickaccess.Skin
import com.autumnstudios.plugins.mercury.api.utils.SkinFetcher
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.v1_20_R1.CraftServer
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList


class NPC(n: String, originalWorld: World, global: Boolean, extenser: NPCExtensive? = null) {

  var name: String
  private val library: ProtocolManager
  var npcExtensive: NPCExtensive?

  var gameProfile: GameProfile? = null
  var entityPlayer: ServerPlayer? = null
  var location: Location;
  val originalWorld: World;

  var skinPlayer: String? = null

  val global: Boolean;







  init {
    this.global = global;
    this.name = n;
    this.library = ProtocolLibrary.getProtocolManager()
    this.npcExtensive = extenser
    this.originalWorld = originalWorld;
    this.location = originalWorld.spawnLocation
    init()
  }

  fun register() {
    val main: Mercury = Mercury.getMercury()
    main.dataStorageNPC.add(getEntityID(), this)
  }



  fun init() {
    build()

    register()
  }

  fun skin(skinName: String? = null) {
    if (skinName != null) {
      skinPlayer = skinName
      build()
      respawn()
      return
    }
    if (skinPlayer != null) {
      val skin: Skin? = SkinFetcher.fetch(SkinFetcher.usernameToUUID(skinPlayer!!))
      if (skin != null) {
        gameProfile!!.properties.put("textures", Property("textures", skin.texture, skin.signature))
      }
    }
  }

  fun changeName(newName: String) {
    name = newName
    build()
    respawn()
  }



  private fun build() {
    val loc: Location = originalWorld.spawnLocation
    val minecraftServer: MinecraftServer = (Bukkit.getServer() as CraftServer).server
    val worldServer: ServerLevel = (loc.world as CraftWorld).handle

    this.gameProfile = GameProfile(UUID.randomUUID(), ColorUtil.colorize(name))

    if (skinPlayer != null) {
      skin()
    }


    this.entityPlayer = ServerPlayer(minecraftServer, worldServer, gameProfile)
  }



  fun move(loc: Location) {
    this.location = loc;

  }

  fun moveAndUpdate(loc: Location) {
    this.location = loc;
    for (p: Player in Bukkit.getOnlinePlayers()) {
      update(p)
    }
  }

  private fun update(p: Player) {
    val connection: ServerGamePacketListenerImpl = (p as CraftPlayer).handle.connection

    val packet: ClientboundTeleportEntityPacket = ReflectionUtils.buildTeleportPacket(location, getEntityID())
    connection.send(packet)
  }

  fun getEntityID() : Int {
    return entityPlayer!!.id
  }



  fun showAll() {
    for (p: Player in Bukkit.getOnlinePlayers()) {
      show(p)
    }
  }

  fun show(p: Player) {
    val connection: ServerGamePacketListenerImpl = (p as CraftPlayer).handle.connection

    connection.send(ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, entityPlayer))
    connection.send(ClientboundAddPlayerPacket(entityPlayer))

    update(p)



  }

  fun removeAll() {
    for (p: Player in Bukkit.getOnlinePlayers()) {
      remove(p)
    }
  }

  fun remove(p: Player) {
    val connection: ServerGamePacketListenerImpl = (p as CraftPlayer).handle.connection
    val list: MutableList<UUID> = ArrayList()
    list.add(entityPlayer?.uuid!!)

    connection.send(ClientboundPlayerInfoRemovePacket(list))
    connection.send(ClientboundRemoveEntitiesPacket(getEntityID()))



  }

  fun respawn() {
    removeAll()
    showAll()
  }


}




