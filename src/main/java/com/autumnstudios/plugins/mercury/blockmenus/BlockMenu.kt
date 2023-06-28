package com.autumnstudios.plugins.mercury.blockmenus

import com.autumnstudios.plugins.mercury.Mercury
import com.autumnstudios.plugins.mercury.chat.ColorUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import kotlin.math.round

class BlockMenu(p: Player) : Listener {

  private val grid: MutableMap<Int, String> = HashMap()
  private val p: Player
  private lateinit var resetLocation: Location

  private lateinit var compareLocation: Location

  private lateinit var startingLocation: Location

  private val RELGRID_0 = Pair<Int, Int>(1, 0);
  private val SCREEN_DISTANCE: Int = 3

  private val entities: MutableList<ArmorStand> = ArrayList()

  init {
      this.p = p
    register()
  }

  private fun register() {
    val instance: Mercury = Mercury.getMercury()
    startingLocation = p.location

    Bukkit.getServer().pluginManager.registerEvents(this, instance)
  }

  fun setGridItem(index: Int, playerName: String) {
    grid[index] = playerName
  }

  fun createAll() {
    for ((i: Int, name: String) in grid.entries) {
      create(i, name)
    }
  }

  @EventHandler
  public fun move(e: PlayerMoveEvent) {
    if (e.player == p) {
      if (round(e.from.x) != round(e.to.x) || round(e.from.z) != round(e.to.z)) {
        e.isCancelled = true
        e.player.teleport(resetLocation)
      }
    }
  }

  @EventHandler
  public fun shift(e: PlayerToggleSneakEvent) {
    if (e.isSneaking) {
      if (e.player == p) {
        e.isCancelled = true
        deregister()

      }
    }
  }

  @EventHandler
  public fun onLeave(e: PlayerQuitEvent) {
    if (e.player == p) {
      deregister()
    }
  }

  @EventHandler
  public fun onJoin(e: PlayerJoinEvent) {
    for (eA: ArmorStand in entities) {
      val eE = eA as Entity
      e.player.hideEntity(Mercury.getMercury(), eE)
    }

  }

  private fun deregister() {
    PlayerMoveEvent.getHandlerList().unregister(this)
    PlayerToggleSneakEvent.getHandlerList().unregister(this)
    PlayerQuitEvent.getHandlerList().unregister(this)
    PlayerJoinEvent.getHandlerList().unregister(this)
    p.sendMessage(ColorUtil().colorize("&a&lEXITED"))

    p.teleport(startingLocation)

    for (e: ArmorStand in entities) {
      val eAsEntity = e as Entity
      eAsEntity.remove()
    }

  }

  private fun create(position: Int, n: String) {
    val locationToModify: Location = p.location
    locationToModify.yaw = -91f
    locationToModify.pitch = 1f
    p.teleport(locationToModify)
    this.resetLocation = locationToModify
    this.compareLocation = resetLocation


    val ARMOR_STAND: ArmorStand = p.world.spawnEntity(relativeToReal(RELGRID_0.first, RELGRID_0.second), EntityType.ARMOR_STAND) as ArmorStand
    ARMOR_STAND.isInvisible = true
    ARMOR_STAND.setGravity(false)
    (ARMOR_STAND as Entity).customName(ColorUtil.getTextComponent("&a&l$n"))
    (ARMOR_STAND as Entity).isCustomNameVisible = true
    Bukkit.getLogger().severe(ARMOR_STAND.toString())
    val ie: ItemStack = ItemStack(Material.PLAYER_HEAD)
    val iMD: SkullMeta = ie.itemMeta as SkullMeta
    iMD.setOwningPlayer(Bukkit.getOfflinePlayer(n))
    ie.itemMeta = iMD
    ARMOR_STAND.equipment.helmet = ie

    for (player: Player in Bukkit.getOnlinePlayers()) {
      player.hideEntity(Mercury.getMercury(), (ARMOR_STAND as Entity))
    }
    entities.add(ARMOR_STAND)
  }

  fun relativeToReal(side: Int, up: Int) : Location {
    val locX = p.location.x + SCREEN_DISTANCE
    val locY = (p.location.y) + up.toDouble()
    val locZ = p.location.z + side.toDouble()

    return Location(p.world, locX, locY, locZ, 91f, 1f)
  }
}
