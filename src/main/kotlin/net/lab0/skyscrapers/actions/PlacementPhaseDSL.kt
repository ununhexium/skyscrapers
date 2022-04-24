package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Position

class PlacementPhaseDSL(val phase: PhaseDSL) {
  fun addBuilder(position: Position): Action = { game ->
    game.addBuilder(phase.player, position)
  }

  fun addBuilder(x: Int, y: Int) =
    addBuilder(Position(x, y))
}