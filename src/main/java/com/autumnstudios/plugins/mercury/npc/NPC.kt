package com.autumnstudios.mercury.nms.npc

import com.autumnstudios.plugins.mercury.Mercury
import com.autumnstudios.plugins.mercury.npc.NPCExtensive
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.mojang.authlib.GameProfile
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket
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


class NPC(n: String, originalWorld: World, extenser: NPCExtensive? = null) {

  private val name: String
  private val library: ProtocolManager
  var npcExtensive: NPCExtensive?

  var gameProfile: GameProfile? = null
  var entityPlayer: ServerPlayer? = null
  val originalWorld: World;





  init {
    this.name = n;
    this.library = ProtocolLibrary.getProtocolManager()
    this.npcExtensive = extenser
    this.originalWorld = originalWorld;
    init()
  }

  fun register() {
    val main: Mercury = Mercury.getInstance()
    main.dataStorageNPC.add(getEntityID(), this)
  }



  fun init() {
    var loc: Location = originalWorld.spawnLocation
    val minecraftServer: MinecraftServer = (Bukkit.getServer() as CraftServer).server
    val worldServer: ServerLevel = (loc.world as CraftWorld).handle

    this.gameProfile = GameProfile(UUID.randomUUID(), name)
    this.entityPlayer = ServerPlayer(minecraftServer, worldServer, gameProfile)

    register()
  }

  fun move(loc: Location) {
    this.entityPlayer!!.teleportTo(loc.x, loc.y, loc.z)
  }

  fun getEntityID() : Int {
    return entityPlayer!!.id
  }

  fun show(p: Player) {
    val connection: ServerGamePacketListenerImpl = (p as CraftPlayer).handle.connection

    connection.send(ClientboundAddPlayerPacket(entityPlayer))

  }


}




