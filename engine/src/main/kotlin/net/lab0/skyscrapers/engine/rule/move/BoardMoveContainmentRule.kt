package net.lab0.skyscrapers.engine.rule.move

import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.MoveOnly
import net.lab0.skyscrapers.engine.rule.AbstractRule

object BoardMoveContainmentRule : AbstractRule<MoveOnly>(
  "Board containment",
  "The movement must start and stay inside the board",
  { state: GameState, turn: MoveOnly ->
    if (turn.start !in state.bounds)
      "Can't move from outside ${turn.start} the board"
    else if (turn.target !in state.bounds)
      "Can't move outside ${turn.target} of the board"
    else null
  }
)
