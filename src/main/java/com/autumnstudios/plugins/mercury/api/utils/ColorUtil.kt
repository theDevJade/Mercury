package com.autumnstudios.plugins.mercury.api.utils

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketContainer
import net.kyori.adventure.text.Component

import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ColorUtil {


      fun flipFlop(text: String) : String {
          val textCompare: String = text
          val newText = text.replace('§', '&')
          if (newText == textCompare) {
              text.replace('&', '§')
          }

          return newText
      }

    fun getTextComponent(text: String) : Component {
      return Component.text(ColorUtil.colorize(text))
    }


    fun colorize(text: String, char: Char = '&') : String {
        return ChatColor.translateAlternateColorCodes(char, text)
    }

    fun send(p: Player, message: String) {
        p.sendMessage(colorize(message))
    }

    fun sendTitle(p: Player, title: String, subtitle: String = "") {
        p.sendTitle(ColorUtil.colorize(title), ColorUtil.colorize(subtitle))
    }

    fun sendActionBar(p: Player, message: String) {
      val pm: ProtocolManager = ProtocolLibrary.getProtocolManager()
      val packet: PacketContainer = PacketContainer(PacketType.Play.Server.SET_ACTION_BAR_TEXT)
      packet.strings.write(0, colorize(message))
      pm.sendServerPacket(p, packet)
    }
}
