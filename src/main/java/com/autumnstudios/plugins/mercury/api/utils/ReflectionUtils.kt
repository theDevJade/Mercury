package com.autumnstudios.plugins.mercury.api.utils

import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.Slime
import org.bukkit.Location

object ReflectionUtils {

  val STATIC_ENTITY = Slime(EntityType.SLIME, MinecraftServer.getServer().allLevels.first())
  fun setDeclaredFieldValue(instance: Any, fieldName: String, value: Any) {
    try {
      val declaredField = instance.javaClass.getDeclaredField(fieldName)
      declaredField.isAccessible = true
      declaredField.set(instance, value)
    } catch (_: Exception) {

    }
  }

  fun buildTeleportPacket(location: Location, entityID: Int) : ClientboundTeleportEntityPacket {

    val packet: ClientboundTeleportEntityPacket = ClientboundTeleportEntityPacket(STATIC_ENTITY)
    ReflectionUtils.setDeclaredFieldValue(packet, "a", entityID)
    ReflectionUtils.setDeclaredFieldValue(packet, "b", location.x)
    ReflectionUtils.setDeclaredFieldValue(packet, "c", location.y)
    ReflectionUtils.setDeclaredFieldValue(packet, "d", location.z)

    return packet

  }

}
