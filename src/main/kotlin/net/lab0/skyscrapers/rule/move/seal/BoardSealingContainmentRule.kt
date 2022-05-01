package net.lab0.skyscrapers.rule.move.seal

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.rule.AbstractRule

object BoardSealingContainmentRule : AbstractRule<TurnType.MoveTurn.SealTurn>(
  "Board containment",
  "The seal must be placed inside the board",
  { state: GameState, turn: TurnType.MoveTurn.SealTurn ->
    if (!state.isWithinBounds(turn.seal))
      "Can't seal outside ${turn.seal} of the board"
    else null
  }
)
