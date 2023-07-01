package com.autumnstudios.plugins.mercury.modules.murky

import com.autumnstudios.mercury.nms.npc.NPC
import com.autumnstudios.plugins.mercury.Mercury
import com.autumnstudios.plugins.mercury.api.sound.QuickSound
import com.autumnstudios.plugins.mercury.api.utils.ColorUtil
import org.bukkit.entity.Player
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Default
import revxrsal.commands.bukkit.player
import revxrsal.commands.command.CommandActor

class MurkyCommand {

  val selectedMap: MutableMap<Player, NPC> = HashMap()

  @Command(*["murky"])
  fun murkyCommand(actor: CommandActor, @Default("") arg1: String, @Default("") arg2param: String) {
    val player: Player = actor.player

    if (!player.hasPermission("murky")) {
      player.sendMessage("No permission to do this")
      QuickSound.play(player, QuickSound.no())
      return
    }
    var arg2 = arg2param
    if (arg1 == "") {
      arg2 = "help"
      return
    }

    when (arg1) {
      "create" -> {
        if (arg2 != "") {
          selectedMap[player] = NPC(arg2, player.world, true)
          selectedMap[player]?.move(player.location)
          selectedMap[player]?.showAll()
        } else {
          ColorUtil.send(player, "&cIncorrect usage, do /help if confused")
          QuickSound.play(player, QuickSound.no())
        }


      }
      "skin" -> {

        if (arg2 != "") {
          selectedMap[player]?.skin(arg2)
          ColorUtil.send(player, "&aAttempting to change skin...")
          QuickSound.play(player, QuickSound.click())
        } else {
          ColorUtil.send(player, "&cIncorrect usage, do /help if confused")
          QuickSound.play(player, QuickSound.no())
        }
      }
      "rename" -> {

        if (arg2 != "") {
          selectedMap[player]?.changeName(arg2)
          ColorUtil.send(player, "&aAttempting to change name...")
          QuickSound.play(player, QuickSound.click())
        } else {
          ColorUtil.send(player, "&cIncorrect usage, do /help if confused")
          QuickSound.play(player, QuickSound.no())
        }
      }

      "selected" -> {
        ColorUtil.send(player, selectedMap[player]?.name!!)
      }

      "select" -> {

        if (arg2 != "") {
          var cpn: NPC? = null
          for (npc: NPC in Mercury.getMercury().dataStorageNPC.dataStorage.values) {
            if (npc.name == arg2) {
              selectedMap[player] = npc
              cpn = npc
              ColorUtil.send(player, "&aSelected &r${npc.name}")
            }

          }
          if (cpn == null) {
            ColorUtil.send(player, "&cCould not find NPC with that name")
          }
        } else {
          ColorUtil.send(player, "&cIncorrect usage, do /help if confused")
          QuickSound.play(player, QuickSound.no())
        }
      }

      "move" -> {
        if (arg2 == "here") {
          selectedMap[player]?.moveAndUpdate(player.location)

        } else {
          ColorUtil.send(player, "&cIncorrect usage, do /help if confused")
          QuickSound.play(player, QuickSound.no())
        }
      }

      "help" -> {
        ColorUtil.send(player, "&c&lMURKY NPC MANAGEMENT")
        ColorUtil.send(player, "&e")
        ColorUtil.send(player, "&e/murky create &r<name>")
        ColorUtil.send(player, "&e/murky skin &r<playername>")
        ColorUtil.send(player, "&e/murky rename &r<newname>")
        ColorUtil.send(player, "&e/murky move here")
        ColorUtil.send(player, "&e/murky selected")
        ColorUtil.send(player, "&e/murky select &r<name>")
        ColorUtil.send(player, "&e")
        ColorUtil.send(player, "&8&l---------------------")
      }
    }

  }
}
