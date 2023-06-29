package com.autumnstudios.plugins.mercury.api.effects

import com.autumnstudios.plugins.mercury.Mercury
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType


class PermPotionManager {

  private val permPotions: MutableMap<Player, PotionEffectType> = HashMap()

  init {

    val instance: Mercury = Mercury.getMercury()

    val taskID: Int = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, Runnable {
                update()
    }, 0L, 100L)
    instance.globalSchedulersData.addGlobalScheduler("permpotions", taskID)


  }

  private fun update() {
    for ((p: Player, effect: PotionEffectType) in permPotions.entries) {
      p.addPotionEffect(PotionEffect(effect, (6 * 20), 1, false, false, false))
    }
  }

  fun addPermPotion(player: Player, effect: PotionEffectType) {
    permPotions[player] = effect
    player.addPotionEffect(PotionEffect(effect, ((6 * 20)), 1, false, false, false))
  }

  fun removePermPotion(player: Player) {
    permPotions.remove(player)
  }

  fun getPermPotion(player: Player) : PotionEffectType? {
    return permPotions[player]
  }
}
