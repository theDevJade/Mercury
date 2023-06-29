package com.autumnstudios.plugins.mercury.api.menus

import com.autumnstudios.plugins.mercury.Mercury
import com.autumnstudios.plugins.mercury.api.sound.QuickSound
import me.flaming.utils.Button
import me.flaming.utils.MenuUtils
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import kotlin.math.tan

class CustomMenu(p: Player, buttons: List<Button>) : Listener {
  private val SCREEN_DISTANCE = 4
  private val playerInfo = HashMap<String, Any>()
  private var player: Player?
  private var screen: Screen? = null
  private var cursor: ItemDisplay? = null
  private var hovered: Entity? = null
  private var hoveredB: Button? = null
  private val main: Mercury;

  private val origButtons: List<Button>

  fun getPlayer() : Player {
    return player!!
  }


    init {
      val main: Mercury = Mercury.getMercury()
      this.main = main
      main.getServer().getPluginManager().registerEvents(this, main)
      player = p
      menuViewers[p] = this
      this.origButtons = buttons
      setupPlayer()
      createScreen()
      createButtons(buttons)
      createCursor(3, 3, Material.SNOWBALL)
      p.sendMessage("Menu opened!")
    }

    private fun setupPlayer() {
        playerInfo["gamemode"] = player!!.gameMode
        playerInfo["location"] = player!!.location
        playerInfo["inventory_contents"] = player!!.inventory.contents
        player!!.gameMode = GameMode.CREATIVE
        main.permPotionsManager.addPermPotion(player!!, PotionEffectType.INVISIBILITY)
        player!!.inventory.clear()
        val newLoc = player!!.location
        newLoc.x = newLoc.blockX + 0.5
        newLoc.y = newLoc.blockY.toDouble()
        newLoc.z = newLoc.blockZ + 0.5
        newLoc.pitch = 0f
        newLoc.yaw = 0f
        player!!.teleport(newLoc)
    }

    fun close() {
      main.permPotionsManager.removePermPotion(player!!)
        HandlerList.unregisterAll(this)
        resetPlayer()
        removeButtons()
        removeCursor()
    }

    private fun createScreen() {
        val screenCorners: List<Location> =
          MenuUtils.getScreenCorners(player!!.location.add(0.0, 1.625, 0.0), SCREEN_DISTANCE)
        screen = Screen(screenCorners, this)
    }

    private fun createButtons(buttons: List<Button>) {
        for (button in buttons) screen!!.createButton(button.x, button.y, button.material, button.name)
    }

    fun createCursor(x: Int, y: Int, material: Material?) {
        val spawnLoc: Location = screen!!.getAbsoluteLocation(x.toDouble(), y.toDouble())
        player!!.world.spawn(spawnLoc, ItemDisplay::class.java) { cursor: ItemDisplay ->
            this.cursor = cursor
            cursor.itemStack = ItemStack(material!!)
            val tr = cursor.transformation
            tr.scale.set(0.75)
            cursor.transformation = tr
        }
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
      if (e.player !== player) return
      val movedFrom: Location = e.getFrom()
      val movedTo: Location = e.getTo()
      val netYaw = -(movedTo.yaw - movedFrom.yaw).toInt()
      val netPitch = -(movedTo.pitch - movedFrom.pitch).toInt()
      moveCursor(netYaw, netPitch)
      e.isCancelled = true
    }

  @EventHandler
  fun onPlayerJoin(e: PlayerJoinEvent) {
    for (entity: Entity in screen?.components!!) {
      e.player.hideEntity(Mercury.getMercury(), entity)
    }
  }

  @EventHandler
  fun onPlayerLeave(e: PlayerQuitEvent) {
    if (e.player == player) {
      close()
    }
  }

  @EventHandler
  fun onSneak(e: PlayerToggleSneakEvent) {
    if (e.player == player) {
      if (e.isSneaking) {
        close()
      }
    }
  }

  @EventHandler
  fun click(e: PlayerInteractEvent) {
    if (e.player == player) {
      if (hoveredB != null) {
        player!!.sendMessage(hoveredB!!.name)
      }
    }
  }

    private fun moveCursor(x: Int, y: Int) {
        val minX: Double = screen?.origin!!.x
        val maxX: Double = minX - screen?.width!!
        val minY: Double = screen?.origin!!.y
        val maxY: Double = minY + screen?.height!!
        val cursorLoc = cursor!!.location
        val cursorDistance = player!!.location.distance(cursor!!.location)
        val resultX = tan(Math.toRadians(x.toDouble())) * cursorDistance
        val resultY = tan(Math.toRadians(y.toDouble())) * cursorDistance
        cursorLoc.add(resultX, resultY, 0.0)
        // Broken asf logic 101
        if (cursorLoc.x > minX) cursorLoc.x = minX
        if (cursorLoc.x < maxX) cursorLoc.x = maxX
        if (cursorLoc.y < minY) cursorLoc.y = minY
        if (cursorLoc.y > maxY) cursorLoc.y = maxY
        val relativeLocation: List<Int> = screen!!.getRelativeLocation(cursorLoc)
        val gridX = relativeLocation[0]
        val gridY = relativeLocation[1]
        val moveLoc: Location = screen!!.getAbsoluteLocation(gridX.toDouble(), gridY.toDouble())
        handleHover(screen!!.getRelativeLocation(cursor!!.location), relativeLocation, screen!!.getComponentsAsList())
        cursor!!.teleport(moveLoc)
      player!!.playSound(player!!.location, QuickSound.click(), 1f, 1f)
    }

    private fun handleHover(oldRelativeLocation: List<Int>, relativeLoc: List<Int>, buttons: List<Entity>) {
        // Handle the old button
        var buttons = buttons
        if (oldRelativeLocation == relativeLoc) return
        if (hovered != null) {
            val resetLocation = hovered!!.location.add(0.0, 0.0, 1.0)
            hovered!!.teleport(resetLocation)
            hovered = null
        }
        buttons = buttons.stream().filter { b: Entity -> b.type == EntityType.ITEM_DISPLAY }
            .toList()
        // Check for new button
        for (b in buttons) {
            val buttonRelativeLoc: List<Int> = screen!!.getRelativeLocation(b.location)
            if (buttonRelativeLoc == relativeLoc) {
                val buttonLoc = b.location.add(0.0, 0.0, -1.0)
                b.teleport(buttonLoc)
                hovered = b
              for (btn: Button in origButtons) {
                if (btn.x == buttonRelativeLoc[0] && btn.y == buttonRelativeLoc[1]) {
                  hoveredB = btn
                }
              }
            }
        }
    }

    private fun resetPlayer() {
        menuViewers.remove(player)
        player!!.gameMode = (playerInfo["gamemode"] as GameMode?)!!
        player!!.removePotionEffect(PotionEffectType.INVISIBILITY)
        player!!.teleport((playerInfo["location"] as Location?)!!)
        player!!.inventory.contents = (playerInfo["inventory_contents"] as Array<ItemStack?>?)!!
        player!!.updateInventory()
        player = null
    }

    private fun removeButtons() {
        for (e in screen!!.getComponentsAsList()) e.remove()
    }

    private fun removeCursor() {
        cursor!!.remove()
    }

    companion object {
        val menuViewers = HashMap<Player?, CustomMenu>()
        fun getMenu(p: Player?): CustomMenu? {
            return menuViewers[p]
        }
    }
}
