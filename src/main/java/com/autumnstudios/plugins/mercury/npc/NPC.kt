package com.autumnstudios.mercury.nms.npc

import com.autumnstudios.mercury.utils.Horizon
import com.autumnstudios.plugins.mercury.Mercury
import com.autumnstudios.plugins.mercury.npc.NPCExtensive
import com.comphenix.protocol.PacketType.Play
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.mojang.authlib.GameProfile
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.Slime
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.v1_20_R1.CraftServer
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.*


class NPC(n: String, originalWorld: World, global: Boolean, extenser: NPCExtensive? = null) {

  private val name: String
  private val library: ProtocolManager
  var npcExtensive: NPCExtensive?

  var gameProfile: GameProfile? = null
  var entityPlayer: ServerPlayer? = null
  var location: Location;
  val originalWorld: World;

  val global: Boolean;

  val STATIC_ENTITY = Slime(EntityType.SLIME, MinecraftServer.getServer().allLevels.first())





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
    var loc: Location = originalWorld.spawnLocation
    val minecraftServer: MinecraftServer = (Bukkit.getServer() as CraftServer).server
    val worldServer: ServerLevel = (loc.world as CraftWorld).handle

    this.gameProfile = GameProfile(UUID.randomUUID(), name)
    this.entityPlayer = ServerPlayer(minecraftServer, worldServer, gameProfile)

    register()
  }

  fun move(loc: Location) {
    this.location = loc;

  }

  fun update(p: Player) {
    val connection: ServerGamePacketListenerImpl = (p as CraftPlayer).handle.connection

    var packet: ClientboundTeleportEntityPacket = getTeleportPacket()
    connection.send(packet)
  }

  fun getEntityID() : Int {
    return entityPlayer!!.id
  }

  fun getTeleportPacket() : ClientboundTeleportEntityPacket {

    val packet: ClientboundTeleportEntityPacket = ClientboundTeleportEntityPacket(STATIC_ENTITY)
    Horizon.setDeclaredFieldValue(packet, "a", getEntityID())
    Horizon.setDeclaredFieldValue(packet, "b", location.x)
    Horizon.setDeclaredFieldValue(packet, "c", location.y)
    Horizon.setDeclaredFieldValue(packet, "d", location.z)

    return packet

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

    connection.send(ClientboundRemoveEntitiesPacket(getEntityID()))
  }


}




