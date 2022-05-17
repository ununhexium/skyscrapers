package net.lab0.skyscrapers.logic.rule.move.seal

import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.AbstractRule

object BoardSealingContainmentRule : AbstractRule<TurnType.MoveTurn.SealTurn>(
  "Board containment",
  "The seal must be placed inside the board",
  { state: GameState, turn: TurnType.MoveTurn.SealTurn ->
    if (turn.seal !in state.bounds)
      "Can't seal outside ${turn.seal} of the board"
    else null
  }
)
