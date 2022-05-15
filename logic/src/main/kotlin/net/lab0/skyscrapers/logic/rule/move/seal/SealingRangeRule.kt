package net.lab0.skyscrapers.logic.rule.move.seal

import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.AbstractRule

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
