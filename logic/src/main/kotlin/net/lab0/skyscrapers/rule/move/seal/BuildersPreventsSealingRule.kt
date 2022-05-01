package net.lab0.skyscrapers.rule.move.seal

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.AbstractRule

object BuildersPreventsSealingRule : AbstractRule<TurnType.MoveTurn.SealTurn>(
  "Building location must be free of builders",
  "When building or sealing, the position where this happens must not have any builder",
  { state: GameState, turn: TurnType.MoveTurn.SealTurn ->
    if (state.builders[turn.seal] != null)
      "Can't seal at ${turn.seal}: a builder is present"
    else null
  }
)
