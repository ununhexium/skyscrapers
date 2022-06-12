package net.lab0.skyscrapers.engine.rule.move.seal

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.AbstractRule

/**
 * Limit the building range of the player
 */
class SealingRangeRule : AbstractRule<TurnType.MoveTurn.SealTurn>(
  "Sealing range limit",
  "The player must seal in the 8 cells around the moved builder",
  { _: GameState, turn: TurnType.MoveTurn.SealTurn ->
    if (turn.target.nextTo(turn.seal)) null
    else "Can't use the builder at ${turn.target} to build at ${turn.seal}"
  }
)
