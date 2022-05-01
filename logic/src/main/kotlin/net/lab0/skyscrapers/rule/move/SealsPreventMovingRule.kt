package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.AbstractRule

object SealsPreventMovingRule : AbstractRule<TurnType.MoveTurn>(
  "Seals prevent movement",
  "Can't move a builder on a position that contains a seal",
  { state: GameState, turn: TurnType.MoveTurn ->
    if (state.seals[turn.target])
      "Can't move to ${turn.target} because it is sealed"
    else null
  }
)
