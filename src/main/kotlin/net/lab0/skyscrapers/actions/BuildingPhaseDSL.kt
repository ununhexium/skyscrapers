package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Position

class BuildingPhaseDSL(val phase: PhaseDSL) {
  /**
   * The only "opportunity" when no move is possible
   */
  fun giveUp(): Action = { game ->
    game.giveUp(phase.player)
  }

  fun moveBuilder(from: Position, to: Position): Action = { game ->
    game.moveBuilder(phase.player, from, to)
  }

  fun moveBuilder(move: MoveBuilderDSL.() -> Unit): Action = { game ->
    val moveDsl = MoveBuilderDSL()
    move(moveDsl)

    game.moveBuilder(phase.player, moveDsl.from, moveDsl.to)
  }
}