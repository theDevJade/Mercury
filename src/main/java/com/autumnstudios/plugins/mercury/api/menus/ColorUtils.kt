package me.flaming.utils

import com.autumnstudios.plugins.mercury.Mercury
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.regex.Pattern

object ColorUtils {
  fun send(player: Player, message: String?) {
      var coloredStr = ChatColor.translateAlternateColorCodes('&', message!!)
      val hexRegex = Pattern.compile("<##[a-zA-Z0-9]*>")
      val matches = hexRegex.matcher(coloredStr)
      while (matches.find()) {
          val hexCode = "#" + matches.group().replace("<##".toRegex(), "").replace(">".toRegex(), "")
          coloredStr =
              coloredStr.replace(matches.group().toRegex(), net.md_5.bungee.api.ChatColor.of(hexCode).toString() + "")
      }
      player.sendMessage(coloredStr)
  }

  fun send(player: Player, message: String?, type: ChatMessageType?) {
      var coloredStr = ChatColor.translateAlternateColorCodes('&', message!!)
      val hexRegex = Pattern.compile("<##[a-zA-Z0-9]*>")
      val matches = hexRegex.matcher(coloredStr)
      while (matches.find()) {
          val hexCode = "#" + matches.group().replace("<##".toRegex(), "").replace(">".toRegex(), "")
          coloredStr =
              coloredStr.replace(matches.group().toRegex(), net.md_5.bungee.api.ChatColor.of(hexCode).toString() + "")
      }
    if (type != null) {
      player.spigot().sendMessage(type, *TextComponent.fromLegacyText(coloredStr))
    }
  }

  fun sendLines(player: Player, messages: List<String?>) {
      for (message in messages) {
          send(player, message)
      }
  }

  fun sendLines(player: Player, messages: List<String?>, type: ChatMessageType?) {
      for (message in messages) {
          send(player, message, type)
      }
  }

  fun getColored(str: String?): String {
    var coloredStr = ChatColor.translateAlternateColorCodes('&', str!!)
    val hexRegex = Pattern.compile("<##[a-zA-Z0-9]*>")
    val matches = hexRegex.matcher(coloredStr)
    while (matches.find()) {
      val hexCode = "#" + matches.group().replace("<##".toRegex(), "").replace(">".toRegex(), "")
      coloredStr = coloredStr.replace(
        matches.group().toRegex(),
        net.md_5.bungee.api.ChatColor.of(hexCode).toString() + ""
      )
    }
    return coloredStr
  }
}
