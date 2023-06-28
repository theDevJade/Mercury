package com.autumnstudios.plugins.mercury.blockmenus

import com.autumnstudios.plugins.mercury.Mercury
import com.autumnstudios.plugins.mercury.chat.ColorUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.RayTraceResult

class BlockMenu(p: Player) : Listener {

  private val gridX: MutableMap<Int, String> = HashMap()
  private val gridY: MutableMap<Int, Int> = HashMap()
  private val p: Player
  private lateinit var resetLocation: Location

  private lateinit var compareLocation: Location

  private lateinit var startingLocation: Location


  private val SCREEN_DISTANCE: Int = 4

  private val entities: MutableList<ArmorStand> = ArrayList()
  private val namespace: NamespacedKey? = NamespacedKey.fromString("menu", Mercury.instance)

  var selectedE: Entity? = null

  init {
      this.p = p
    register()
  }

  private fun register() {
    val instance: Mercury = Mercury.getMercury()
    startingLocation = p.location

    Bukkit.getServer().pluginManager.registerEvents(this, instance)
  }

  fun setGridItem(x: Int, y: Int, playerName: String) {
    gridX[x] = playerName
    gridY[x] = y
  }

  fun createAll() {
    for ((i: Int, name: String) in gridX.entries) {
      create(i, gridY[i]!!, name)

    }

  }

  @EventHandler
  fun move(e: PlayerMoveEvent) {
    if (e.player == p) {
      if (e.from.x != e.to.x || e.from.z != e.to.z) {
        resetLocation = Location(p.world, resetLocation.x, resetLocation.y, resetLocation.z, e.to.yaw, e.to.pitch)
        e.player.teleport(resetLocation)
      }
      if (e.from.pitch != e.to.pitch || e.from.yaw != e.to.yaw) {
        trace()
      }
    }
  }



  private fun trace() {
    val raytrace: RayTraceResult? = p.rayTraceEntities(SCREEN_DISTANCE + 3)


    if (raytrace?.hitEntity != null) {
      if (selectedE != raytrace.hitEntity) {
        selectedE = raytrace.hitEntity
        returnBack()

      }
    } else {
      if (selectedE != null) {

        returnForward()
      }
      selectedE = null

    }

  }

  private fun returnBack() {
    if (selectedE != null) {

      val modifiedLoc = selectedE?.location
      modifiedLoc?.x = modifiedLoc?.x?.minus(1)!!
      selectedE?.teleport(modifiedLoc)


    }

  }
  private fun returnForward() {
    if (selectedE != null) {
      val modifiedLoc = selectedE?.location
      modifiedLoc?.x = modifiedLoc?.x?.plus(1)!!
      selectedE?.teleport(modifiedLoc)
    }

  }

  @EventHandler
  fun shift(e: PlayerToggleSneakEvent) {
    if (e.isSneaking) {
      if (e.player == p) {
        e.isCancelled = true
        deregister()

      }
    }
  }

  @EventHandler
  fun onLeave(e: PlayerQuitEvent) {
    if (e.player == p) {
      deregister()
    }
  }

  @EventHandler
  fun onJoin(e: PlayerJoinEvent) {
    for (eA: ArmorStand in entities) {
      val eE = eA as Entity
      e.player.hideEntity(Mercury.getMercury(), eE)
    }

  }

  @EventHandler
  fun interact(e: PlayerInteractAtEntityEvent) {
    if (e.player == p) {
      p.sendMessage("Test")
      p.sendMessage("$selectedE")
      if (selectedE != null) {
        for ((i: Int, name: String) in gridX.entries) {


          if (ColorUtil().colorize(name) == e.rightClicked.customName) {
            p.sendMessage(i.toString())
          }

        }
      }

    }
  }

  private fun deregister() {
    PlayerMoveEvent.getHandlerList().unregister(this)
    PlayerToggleSneakEvent.getHandlerList().unregister(this)
    PlayerQuitEvent.getHandlerList().unregister(this)
    PlayerJoinEvent.getHandlerList().unregister(this)
    PlayerInteractAtEntityEvent.getHandlerList().unregister(this)
    p.sendMessage(ColorUtil().colorize("&a&lEXITED"))

    p.teleport(startingLocation)

    for (e: ArmorStand in entities) {
      val eAsEntity = e as Entity
      eAsEntity.remove()
    }

  }

  private fun create(x: Int, y: Int, n: String) {
    val locationToModify: Location = p.location
    locationToModify.yaw = -91f
    locationToModify.pitch = 1f
    p.teleport(locationToModify)
    this.resetLocation = locationToModify
    this.compareLocation = resetLocation




    val ARMOR_STAND: ArmorStand = p.world.spawnEntity(relativeToReal(y, x), EntityType.ARMOR_STAND) as ArmorStand
    ARMOR_STAND.isInvisible = true
    ARMOR_STAND.setGravity(false)
    (ARMOR_STAND as Entity).customName(ColorUtil.getTextComponent(n))
    (ARMOR_STAND as Entity).isCustomNameVisible = true


    Bukkit.getLogger().severe(ARMOR_STAND.toString())
    val ie = ItemStack(Material.PLAYER_HEAD)
    val iMD: SkullMeta = ie.itemMeta as SkullMeta
    iMD.setOwningPlayer(Bukkit.getOfflinePlayer(n))
    ie.itemMeta = iMD
    ARMOR_STAND.equipment.helmet = ie


    if (namespace != null) {
      (ARMOR_STAND as Entity).persistentDataContainer.set(namespace, PersistentDataType.INTEGER, x)
    }


    for (player: Player in Bukkit.getOnlinePlayers()) {
      if (player != p) {
        player.hideEntity(Mercury.getMercury(), (ARMOR_STAND as Entity))
      }

    }
    entities.add(ARMOR_STAND)
  }

  fun relativeToReal(side: Int, up: Int) : Location {
    val locX = p.location.x + SCREEN_DISTANCE
    val locY = (p.location.y) + up.toDouble()
    val locZ = (p.location.z) + side.toDouble()

    return Location(p.world, locX, locY, locZ, 91f, 1f)
  }

  fun realToRelative(z: Int, y: Int) : Location {
    val locX = p.location.x - SCREEN_DISTANCE
    val locY = (p.location.y) - y.toDouble()
    val locZ = (p.location.z) - z.toDouble()

    return Location(p.world, locX, locY, locZ, 91f, 1f)
  }
}
