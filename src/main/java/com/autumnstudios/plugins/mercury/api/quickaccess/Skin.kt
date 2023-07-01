package com.autumnstudios.plugins.mercury.api.quickaccess

class Skin(texture: String, signature: String) {

  val texture: String
  val signature: String

  init {
    this.texture = texture
    this.signature = signature
  }
}
