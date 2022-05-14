package net.lab0.skyscrapers.logic.rule.move

import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.Move
import net.lab0.skyscrapers.logic.rule.AbstractRule

object BoardMoveContainmentRule : AbstractRule<Move>(
  "Board containment",
  "The movement must start and stay inside the board",
  { state: GameState, turn: Move ->
    if (turn.start !in state.bounds)
      "Can't move from outside ${turn.start} the board"
    else if (turn.target !in state.bounds)
      "Can't move outside ${turn.target} of the board"
    else null
  }
)
