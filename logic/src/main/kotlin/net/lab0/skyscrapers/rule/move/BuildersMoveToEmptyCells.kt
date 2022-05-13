package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.Move
import net.lab0.skyscrapers.rule.AbstractRule

object BuildersMoveToEmptyCells : AbstractRule<Move>(
  "Builders move to empty cells",
  "Checks that the player moves an existing builder to an empty cell",
  { state: GameState, turn: Move ->
    if (state.builders[turn.start] == null)
      "There is no builder at ${turn.start}"
    else if (state.builders[turn.target] != null)
      "There is already a builder from player#${state.builders[turn.target]} at ${turn.target}"
    else if (turn.start == turn.target)
      "The builder at ${turn.start} didn't move"
    else null
  }
)
