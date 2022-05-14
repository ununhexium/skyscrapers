package net.lab0.skyscrapers.logic.rule.move.seal

import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.AbstractRule

object SealsPreventSealingRule : net.lab0.skyscrapers.logic.rule.AbstractRule<TurnType.MoveTurn.SealTurn>(
  "Seals prevent sealing",
  "",
  { state: GameState, turn: TurnType.MoveTurn.SealTurn ->
    if (state.seals[turn.seal])
      "Can't seal at ${turn.seal} because it is already sealed"
    else null
  }
)