package net.lab0.skyscrapers.engine.rule.move.seal

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.AbstractRule

object BoardSealingContainmentRule : AbstractRule<TurnType.MoveTurn.SealTurn>(
  "Board containment",
  "The seal must be placed inside the board",
  { state: GameState, turn: TurnType.MoveTurn.SealTurn ->
    if (turn.seal !in state.bounds)
      "Can't seal outside ${turn.seal} of the board"
    else null
  }
)
