package com.ascorapvp.refine.tracker

import com.autumnstudios.plugins.mercury.api.quickaccess.WrappedPacketBlock
import com.github.retrooper.packetevents.util.Vector3i
import java.util.UUID

object BlockTracker {

    val blocks: MutableMap<UUID, WrappedPacketBlock> = mutableMapOf()

    fun isPacketBlock(player: UUID, location: Vector3i, world: String): Boolean {
        return blocks[player]?.compareLoc(location, world) == true
    }
}
