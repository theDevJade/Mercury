package com.autumnstudios.plugins.mercury.api.math

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

object FlatoutVector {

  enum class VectorType {
    Vector3D,
    Vector3I,
    Vector3F,
    VectorBlock
  }
  fun locToVec(location: Location) : Vector {
    return location.toVector()
  }

  fun vecToLoc(vector: Vector, world: World, yaw: Float = 0f, pitch: Float = 0f) : Location {
    return vector.toLocation(world, yaw, pitch)
  }

  fun convertVector(type: VectorType, vector: Vector) : Any {
    return when (type) {
      VectorType.Vector3D -> {
        vector.toVector3d()
      }

      VectorType.Vector3I -> {
        vector.toVector3i()
      }

      VectorType.Vector3F -> {
        vector.toVector3f()
      }

      VectorType.VectorBlock -> {
        vector.toBlockVector()
      }
    }
  }
}
