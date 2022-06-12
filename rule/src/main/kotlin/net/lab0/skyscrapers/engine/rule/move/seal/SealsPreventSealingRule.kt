package net.lab0.skyscrapers.engine.rule.move.seal

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.AbstractRule

object SealsPreventSealingRule : AbstractRule<TurnType.MoveTurn.SealTurn>(
  "Seals prevent sealing",
  "",
  { state: GameState, turn: TurnType.MoveTurn.SealTurn ->
    if (state.seals[turn.seal])
      "Can't seal at ${turn.seal} because it is already sealed"
    else null
  }
)