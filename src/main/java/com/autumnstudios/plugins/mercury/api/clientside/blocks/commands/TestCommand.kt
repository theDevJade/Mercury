package com.ascorapvp.refine.commands

import com.ascorapvp.refine.tracker.BlockTracker
import com.ascorapvp.refine.utils.WrappedLocation
import com.ascorapvp.refine.utils.WrappedPacketBlock
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket
import net.minecraft.world.level.block.Block
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import revxrsal.commands.annotation.Command
import revxrsal.commands.bukkit.player
import revxrsal.commands.command.CommandActor

object TestCommand {

    @Command(*["testblock"])
    fun test(actor: CommandActor, id: Int) {
      val player = actor.player
      // TODO: https://minecraft-ids.grahamedgecombe.com/
      // raw implementation TODO: write packet wrappers and stuff

      val wrappedBlock = WrappedPacketBlock(
          WrappedLocation(player.location.blockX, player.location.blockY, player.location.blockZ, player.world.name),
          Block.stateById(id)
      )

      (player as CraftPlayer).handle.connection.send(
          ClientboundBlockUpdatePacket(
              BlockPos(wrappedBlock.location.x, wrappedBlock.location.y, wrappedBlock.location.z),
              wrappedBlock.block
          )
      )

      BlockTracker.blocks[player.uniqueId] = wrappedBlock

      player.sendMessage("Sent change")
    }
}
