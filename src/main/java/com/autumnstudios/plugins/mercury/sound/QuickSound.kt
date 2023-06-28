package com.autumnstudios.plugins.mercury.sound

import org.bukkit.Sound

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
}
