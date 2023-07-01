package com.autumnstudios.plugins.mercury.api.math

class Radian(radian: Double) {

  private val radian: Double

  init {
      this.radian = radian
  }

  fun get() : Double {
    return radian
  }
}
