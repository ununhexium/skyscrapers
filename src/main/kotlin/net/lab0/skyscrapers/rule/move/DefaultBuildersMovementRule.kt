package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.AbstractRule

object DefaultBuildersMovementRule : AbstractRule<TurnType.MoveTurn>(
  "Check builders are moved",
  "Checks that the player moves an existing builder of their own and that it's moved to an empty cell",
  { state: GameState, turn: TurnType.MoveTurn ->
    if (state.builders[turn.start] == null)
      "There is no builder that belongs to player#${turn.player} at ${turn.start}"
    else if (state.builders[turn.start] != turn.player)
      "The builder at ${turn.start} doesn't belong to player#${turn.player}. It belongs to player#${state.builders[turn.start]}"
    else if (state.builders[turn.target] != null)
      "There is already a builder from player#${turn.player} at ${turn.target}"
    else if (turn.start == turn.target)
      "The builder at ${turn.start} didn't move"
    else null
  }
)
