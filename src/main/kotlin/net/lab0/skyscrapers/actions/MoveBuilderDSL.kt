package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Position

class MoveBuilderDSL() {
  lateinit var from: Position
  lateinit var to: Position

  fun from(x: Int, y: Int) {
    from = Position(x,y)
  }
  fun to(x: Int, y: Int) {
    to = Position(x,y)
  }
}
