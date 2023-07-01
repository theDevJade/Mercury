package com.ascorapvp.refine.listener

import com.ascorapvp.refine.tracker.BlockTracker
import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging
import org.bukkit.Bukkit
import org.bukkit.entity.Player


class PacketListener : PacketListenerAbstract(PacketListenerPriority.HIGHEST) {

    override fun onPacketReceive(event: PacketReceiveEvent?) {
        if (event == null) return

        if (event.packetType == PacketType.Play.Client.PLAYER_DIGGING) {
            val wrappedPacket = WrapperPlayClientPlayerDigging(event)



            // TODO: we may need to send a block Ack packet (Agnowledge)
            if (BlockTracker.isPacketBlock((event.player as Player).uniqueId, wrappedPacket.blockPosition, (event.player as Player).world.name)) {
                Bukkit.broadcastMessage("Recieved a packet mine block from ${(event.player as Player).name}")
                event.isCancelled = true
            }
        } else if (event.packetType == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            val wrappedPacket = WrapperPlayClientPlayerBlockPlacement(event)



            if (BlockTracker.isPacketBlock((event.player as Player).uniqueId, wrappedPacket.blockPosition, (event.player as Player).world.name)) {
                event.isCancelled = true
            }
        }
    }
}
