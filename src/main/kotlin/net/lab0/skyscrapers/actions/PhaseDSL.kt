package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Game
import net.lab0.skyscrapers.Position

class PhaseDSL(private val player: Int) {
  fun addBuilder(position: Position): (Game) -> Unit {
    return { game ->
      game.addBuilder(player, position)
    }
  }

  fun addBuilder(x: Int, y: Int) =
    addBuilder(Position(x, y))
}
