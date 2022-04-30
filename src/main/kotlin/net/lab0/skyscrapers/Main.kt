package net.lab0.skyscrapers

import net.lab0.skyscrapers.actions.DSL

fun main() {
  val g = Game.new(5, 5, 2, 1)

  g.play {
    DSL.player(1).placement.addBuilder(0, 0)
  }
}
