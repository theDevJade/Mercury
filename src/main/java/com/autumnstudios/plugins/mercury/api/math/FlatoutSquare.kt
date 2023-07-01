package com.autumnstudios.plugins.mercury.api.math

import org.bukkit.util.Vector

class FlatoutSquare(corner1: Vector, corner2: Vector, corner3: Vector, corner4: Vector) {

  val corner1: Vector
  val corner2: Vector
  val corner3: Vector
  val corner4: Vector

  init {
      this.corner1 = corner1
    this.corner2 = corner2
    this.corner3 = corner3
    this.corner4 = corner4
  }
}
