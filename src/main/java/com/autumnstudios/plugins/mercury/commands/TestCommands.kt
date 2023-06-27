package com.autumnstudios.plugins.mercury.commands

import com.autumnstudios.mercury.nms.npc.NPC
import com.autumnstudios.plugins.mercury.chat.ColorUtil
import com.autumnstudios.plugins.mercury.npc.ClickType
import com.autumnstudios.plugins.mercury.npc.NPCExtensive
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Default
import revxrsal.commands.bukkit.player
import revxrsal.commands.command.CommandActor

class TestCommands {


  @Command(*["daddynpc", "daddy"])
  fun echo(actor: CommandActor, @Default("") message: String?) {
    val testingNPCExtensive = NPCExtensive{ clickType: ClickType, player: Player ->
      player.kick(ColorUtil.getTextComponent("&c&lHE DENIED YOU BECAUSE OF YOUR SMALL DICK"))
    }

    var msg = message?.let { ColorUtil().colorize(it) }
    if (msg == null) msg = "my dick"
    val player: Player = actor.player;
    val npc: NPC = NPC(msg, player.world, true, testingNPCExtensive)
    npc.move(player.location)
    npc.show(player)
  }
}
