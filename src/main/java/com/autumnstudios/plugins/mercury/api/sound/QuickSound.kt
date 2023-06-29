package com.autumnstudios.plugins.mercury.api.sound

import org.bukkit.Sound
import org.bukkit.entity.Player


object QuickSound {

  fun click() : Sound {
    return Sound.UI_BUTTON_CLICK
  }

  fun exp() : Sound {
    return Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
  }

  fun yes() : Sound {
    return Sound.ENTITY_VILLAGER_YES
  }

  fun no() : Sound {
    return Sound.ENTITY_VILLAGER_NO
  }

  fun play(p: Player, s: Sound) {
    p.playSound(p.location, s, 1f, 1f)
  }
}
