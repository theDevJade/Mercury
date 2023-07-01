package com.autumnstudios.plugins.mercury.api.math

class FlatoutBox(square1: FlatoutSquare, square2: FlatoutSquare) {

  val square1: FlatoutSquare
  val square2: FlatoutSquare

  init {
      this.square1 = square1
    this.square2 = square2
  }
}
