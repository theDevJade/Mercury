package com.autumnstudios.plugins.mercury.api.math

import kotlin.math.PI

object FlatoutAngles {

  fun radiansToDegrees(radians: Double): Double {
    return radians * 180.0 / PI
  }

  fun degreesToRadians(degrees: Double): Double {
    return degrees * PI / 180.0
  }

  fun quickRadian(degrees: Double): Radian {
    return Radian(degreesToRadians(degrees))
  }
}
