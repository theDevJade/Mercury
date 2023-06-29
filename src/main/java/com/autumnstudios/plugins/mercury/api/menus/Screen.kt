package com.autumnstudios.plugins.mercury.api.menus

import com.autumnstudios.plugins.mercury.Mercury
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.abs
import kotlin.math.roundToInt

class Screen(locs: List<Location>, extensive: CustomMenu) {
    private val world: World
    private val topleft: Location
    private val topright: Location
    private val bottomleft: Location
    private val bottomright: Location
    val origin: Location
    val width: Int
    val height: Int
    var components: MutableList<Entity> = ArrayList()

  val menu: CustomMenu


    init {
      world = locs[0].world
      topleft = locs[0]
      topright = locs[1]
      bottomleft = locs[2]
      bottomright = locs[3]
      origin = bottomleft
      width = topleft.blockX - topright.blockX
      height = topleft.blockY - bottomleft.blockY

      this.menu = extensive
    }

    fun createButton(x: Int, y: Int, material: Material?, name: String) {
        val spawnLoc = getAbsoluteLocation(x.toDouble(), y.toDouble())
        world.spawn(spawnLoc, ItemDisplay::class.java) { button: ItemDisplay ->
          button.itemDisplayTransform = ItemDisplay.ItemDisplayTransform.FIXED
          button.itemStack = ItemStack(material!!)
          for (plyr: Player in Bukkit.getOnlinePlayers()) {
            if (plyr != menu.getPlayer()) {
              plyr.hideEntity(Mercury.getMercury(), (button as Entity))
            }
          }

          components.add(button)
        }
        createName(spawnLoc, name)
    }

    private fun createName(spawnLoc: Location, name: String) {
        world.spawn(spawnLoc.add(0.0, -1.9, 0.0), ArmorStand::class.java) { armorStand: ArmorStand ->
            armorStand.isVisible = false
            armorStand.setGravity(false)
            armorStand.customName = name
            armorStand.isCustomNameVisible = true
            components.add(armorStand)
        }
    }

    fun addComponent(e: Entity) {
        components.add(e)
    }

    fun getComponentsAsList(): List<Entity> {
        return components
    }

    fun getAbsoluteLocation(x: Double, y: Double): Location {
        val absX = origin.x - x / GRID_SCALE_X * width
        val absY = origin.y + y / GRID_SCALE_Y * height
        val absZ = origin.z
        return Location(origin.world, absX, absY, absZ)
    }

    fun getRelativeLocation(absoluteLoc: Location): List<Int> {
        val list: MutableList<Int> = ArrayList()
        val relX = (origin.x - absoluteLoc.x) * GRID_SCALE_X / width
        val relY = (origin.y - absoluteLoc.y) * GRID_SCALE_Y / height
        list.add(abs(relX.roundToInt()))
        list.add(abs(relY.roundToInt()))
        return list
    }

    companion object {
        private const val GRID_SCALE_X = 6
        private const val GRID_SCALE_Y = 6
    }
}
