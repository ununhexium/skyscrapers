package net.lab0.skyscrapers.rule.move.seal

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.AbstractRule

/**
 * Limit the building range of the player
 */
class SealingRangeRule : AbstractRule<TurnType.MoveTurn.SealTurn>(
  "Building range limit",
  "The player must build in the 8 cells around the moved builder",
  { state: GameState, turn: TurnType.MoveTurn.SealTurn ->
    if (turn.target.nextTo(turn.seal)) null
    else "Can't use the builder at ${turn.target} to build at ${turn.seal}"
  }
)
