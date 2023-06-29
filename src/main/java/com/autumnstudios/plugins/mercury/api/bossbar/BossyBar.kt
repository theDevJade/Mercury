package com.autumnstudios.plugins.mercury.api.bossbar

import com.autumnstudios.plugins.mercury.api.utils.ColorUtil
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player

object BossyBar {

  fun send(p: Player, bar: String, color: BarColor = BarColor.RED) {
    val bossBar: BossBar = Bukkit.createBossBar(ColorUtil.colorize(bar), color, BarStyle.SOLID)
    bossBar.isVisible = true
    bossBar.addPlayer(p)


  }
}
