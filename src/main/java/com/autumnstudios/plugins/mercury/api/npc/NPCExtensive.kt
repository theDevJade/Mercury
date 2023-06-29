package com.autumnstudios.plugins.mercury.api.npc

import org.bukkit.entity.Player

fun interface NPCExtensive {

  fun onClick(clickType: ClickType, p: Player)


}
