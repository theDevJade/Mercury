package com.autumnstudios.plugins.mercury.api.math

import com.expression.parser.Parser
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin


object FlatoutMath {

  fun parse(s: String) : Double? {
    return try {
      Parser.simpleEval(s)
    } catch (e: Exception) {
      null
    }
  }


    fun generateParabolicCurve(start: Location, end: Location, height: Double, points: Int): List<Location> {
        val curve = mutableListOf<Location>()

        val startVec = start.toVector()
        val endVec = end.toVector()
        val curveVec = endVec.clone().subtract(startVec)

        for (i in 0..points) {
            val t = i.toDouble() / points.toDouble()
            val x = startVec.x + curveVec.x * t
            val y = startVec.y + curveVec.y * t + calculateParabolicHeight(height, t)
            val z = startVec.z + curveVec.z * t

            val point = Location(start.world, x, y, z)
            curve.add(point)
        }

        return curve
    }

  fun generateLine(world: World?, start: Vector, end: Vector, density: Double): List<Location>? {
    val locations: MutableList<Location> = ArrayList()
    val direction: Vector = end.clone().subtract(start).normalize()
    val distance: Double = start.distance(end)
    val numPoints = (distance / density).toInt()
    val interval = distance / numPoints
    for (i in 0 until numPoints) {
      val point: Vector = direction.clone().multiply(interval * i).add(start)
      val location = Location(world, point.x, point.y, point.z)
      locations.add(location)
    }
    return locations
  }

  fun pointsFromBox(
    world: World?, box: FlatoutBox): List<Location> {
    val corner1 = box.square1.corner1
    val corner2 = box.square1.corner2
    val corner3 = box.square1.corner3
    val corner4 = box.square1.corner4
    val corner5 = box.square2.corner1
    val corner6 = box.square2.corner2
    val corner7 = box.square2.corner3
    val corner8 = box.square2.corner4

    val points: MutableList<Location> = ArrayList()
    val minX = corner1.blockX.coerceAtMost(corner2.blockX).coerceAtMost(Math.min(corner3.blockX, corner4.blockX))
      .coerceAtMost(corner5.blockX.coerceAtMost(corner6.blockX).coerceAtMost(Math.min(corner7.blockX, corner8.blockX)))
    val minY = corner1.blockY.coerceAtMost(corner2.blockY).coerceAtMost(Math.min(corner3.blockY, corner4.blockY))
      .coerceAtMost(corner5.blockY.coerceAtMost(corner6.blockY).coerceAtMost(Math.min(corner7.blockY, corner8.blockY)))
    val minZ = corner1.blockZ.coerceAtMost(corner2.blockZ).coerceAtMost(Math.min(corner3.blockZ, corner4.blockZ))
      .coerceAtMost(corner5.blockZ.coerceAtMost(corner6.blockZ).coerceAtMost(Math.min(corner7.blockZ, corner8.blockZ)))
    val maxX = corner1.blockX.coerceAtLeast(corner2.blockX).coerceAtLeast(Math.max(corner3.blockX, corner4.blockX))
      .coerceAtLeast(corner5.blockX.coerceAtLeast(corner6.blockX).coerceAtLeast(Math.max(corner7.blockX, corner8.blockX)))
    val maxY = corner1.blockY.coerceAtLeast(corner2.blockY).coerceAtLeast(Math.max(corner3.blockY, corner4.blockY))
      .coerceAtLeast(corner5.blockY.coerceAtLeast(corner6.blockY).coerceAtLeast(Math.max(corner7.blockY, corner8.blockY)))
    val maxZ = corner1.blockZ.coerceAtLeast(corner2.blockZ).coerceAtLeast(Math.max(corner3.blockZ, corner4.blockZ))
      .coerceAtLeast(corner5.blockZ.coerceAtLeast(corner6.blockZ).coerceAtLeast(Math.max(corner7.blockZ, corner8.blockZ)))
    for (x in minX..maxX) {
      for (y in minY..maxY) {
        for (z in minZ..maxZ) {
          val location = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
          points.add(location)
        }
      }
    }
    return points
  }

  fun generateSphere3D(world: World, center: Location, radius: Double): List<Location> {
    val locations = mutableListOf<Location>()
    val minX = floor(center.x - radius).toInt()
    val minY = floor(center.y - radius).toInt()
    val minZ = floor(center.z - radius).toInt()
    val maxX = floor(center.x + radius).toInt()
    val maxY = floor(center.y + radius).toInt()
    val maxZ = floor(center.z + radius).toInt()

    for (x in minX..maxX) {
      for (y in minY..maxY) {
        for (z in minZ..maxZ) {
          val location = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
          if (center.distance(location) <= radius) {
            locations.add(location)
          }
        }
      }
    }

    return locations
  }

  fun generateCircle3D(world: World, center: Location, radius: Double, rotation: Radian): List<Location> {
    val locations = mutableListOf<Location>()
    val centerX = center.x
    val centerY = center.y
    val centerZ = center.z

    val numPoints = 360
    val angleIncrement = 2 * Math.PI / numPoints

    for (i in 0 until numPoints) {
      val angle = i * angleIncrement + rotation.get()
      val x = centerX + radius * cos(angle)
      val z = centerZ + radius * sin(angle)
      val location = Location(world, x, centerY, z)
      locations.add(location)
    }

    return locations
  }



    private fun calculateParabolicHeight(height: Double, t: Double): Double {
        return -4 * height * t * (t - 1)
    }
}
