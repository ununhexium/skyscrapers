package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Position

class AddBuilderDSL {
  lateinit var position: Position

  fun position(position: Position) {
    this.position = position
  }
}
