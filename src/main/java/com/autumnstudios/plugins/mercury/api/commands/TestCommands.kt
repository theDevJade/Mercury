package com.autumnstudios.plugins.mercury.api.commands

import com.autumnstudios.mercury.nms.npc.NPC
import com.autumnstudios.plugins.mercury.api.menus.CustomMenu
import com.autumnstudios.plugins.mercury.api.npc.ClickType
import com.autumnstudios.plugins.mercury.api.npc.NPCExtensive
import com.autumnstudios.plugins.mercury.api.utils.ColorUtil
import me.flaming.utils.Button
import org.bukkit.Material
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Default
import revxrsal.commands.bukkit.player
import revxrsal.commands.command.CommandActor


class TestCommands {


  @Command(*["daddynpc", "daddy"])
  fun echo(actor: CommandActor, @Default("") message: String?) {
    val testingNPCExtensive = NPCExtensive{ clickType: ClickType, player: Player ->
      player.sendMessage(ColorUtil.getTextComponent("&c&lHE DENIED YOU BECAUSE OF YOUR SMALL DICK"))
    }

    var msg = message?.let { ColorUtil.colorize(it) }
    if (msg == null) msg = "my dick"
    val player: Player = actor.player;
    val npc: NPC = NPC(msg, player.world, true, testingNPCExtensive)
    npc.move(player.location)
    npc.showAll()
  }

  @Command(*["menu", "blockmenu"])
  fun blockmenu(actor: CommandActor, @Default("") message: String?) {

    val buttons: MutableList<Button> = ArrayList<Button>()
    buttons.add(Button(4, 2, Material.PLAYER_HEAD, "Test"))
    val menu: CustomMenu = CustomMenu(actor.player, buttons)
  }
}
