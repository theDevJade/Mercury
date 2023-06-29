package com.ascorapvp.refine.utils

import com.github.retrooper.packetevents.util.Vector3i
import net.minecraft.world.level.block.state.BlockState


data class WrappedPacketBlock(
    val location: WrappedLocation,
    val block: BlockState
) {
    fun compareLoc(location: Vector3i, world: String): Boolean {
        return location.x == this.location.x && location.y == this.location.y && this.location.z == location.z && world == this.location.worldName
    }
}
