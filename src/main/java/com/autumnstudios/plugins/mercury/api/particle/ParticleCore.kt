package com.autumnstudios.plugins.mercury.api.particle

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.entity.Player

class ParticleCore {

    fun spawn(eff: Particle, x: Double, y: Double, z: Double, world: World) {
        world.spawnParticle(eff, Location(world, x, y, z), 1);

    }

    fun spawn(eff: EnumWrappers.Particle, x: Float, y: Float, z: Float, player: Player) {
        val manager: ProtocolManager = ProtocolLibrary.getProtocolManager();

        val packet: PacketContainer = manager.createPacket(PacketType.Play.Server.WORLD_PARTICLES)

        packet.particles.write(0, eff)
        packet.float.write(0, x).write(1, y).write(2, z)
        manager.sendServerPacket(player, packet)
    }

  fun spawnFromList(eff: EnumWrappers.Particle, locs: List<Location>, p: Player) {
    for (loc: Location in locs) {
      spawn(eff, loc.x.toFloat(), loc.y.toFloat(), loc.z.toFloat(), p)
    }
  }
}
