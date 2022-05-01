package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.AbstractRule

object BoardMoveContainmentRule : AbstractRule<TurnType.MoveTurn>(
  "Board containment",
  "The movement must start and stay inside the board",
  { state: GameState, turn: TurnType.MoveTurn ->
    if (!state.isWithinBounds(turn.start))
      "Can't move from outside ${turn.start} the board"
    else if (!state.isWithinBounds(turn.target))
      "Can't move outside ${turn.target} of the board"
    else null
  }
)
