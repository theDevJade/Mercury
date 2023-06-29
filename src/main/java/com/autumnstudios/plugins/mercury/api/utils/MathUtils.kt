package com.autumnstudios.plugins.mercury.api.utils

import org.bukkit.Location

object MathUtils {
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

    private fun calculateParabolicHeight(height: Double, t: Double): Double {
        return -4 * height * t * (t - 1)
    }
}
