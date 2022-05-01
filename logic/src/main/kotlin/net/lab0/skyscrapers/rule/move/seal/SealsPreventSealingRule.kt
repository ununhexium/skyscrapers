package net.lab0.skyscrapers.rule.move.seal

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.AbstractRule

object SealsPreventSealingRule : AbstractRule<TurnType.MoveTurn.SealTurn>(
  "Seals prevent sealing",
  "",
  { state: GameState, turn: TurnType.MoveTurn.SealTurn ->
    if (state.seals[turn.seal])
      "Can't seal at ${turn.seal} because it is already sealed"
    else null
  }
)