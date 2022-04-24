package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Position

class PhaseDSL(private val player: Int) {
  fun addBuilder(position: Position): Action = { game ->
    game.addBuilder(player, position)
  }

  fun addBuilder(x: Int, y: Int) =
    addBuilder(Position(x, y))

  /**
   * The only "opportunity" when no move is possible
   */
  fun giveUp(): Action = { game ->
    game.giveUp(player)
  }

  fun moveBuilder(from: Position, to:Position): Action = { game ->
    game.moveBuilder(player, from, to)
  }
}
