package me.flaming.utils

import org.bukkit.Location

object MenuUtils {
    fun getScreenCorners(source: Location, distance: Int): List<Location> {
        val x = source.x
        val y = source.y
        val z = source.z
        val topLeft = Location(source.world, x + distance, y + distance, z + distance)
        val topRight = Location(source.world, x - distance, y + distance, z + distance)
        val bottomLeft = Location(source.world, x + distance, y - distance, z + distance)
        val bottomRight = Location(source.world, x - distance, y - distance, z + distance)
        return listOf(topLeft, topRight, bottomLeft, bottomRight)
    }
}
