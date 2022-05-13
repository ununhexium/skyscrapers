package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.Move
import net.lab0.skyscrapers.rule.AbstractRule

class MovementRangeRule : AbstractRule<Move>(
  "Movement limitation",
  "Checks that the builder doesn't go further than the tiles around it.",
  { _: GameState, turn: Move ->
    if (!turn.start.nextTo(turn.target))
      "The builder can't be moved from ${turn.start} to ${turn.target} because it's more than 1 tile away"
    else null
  }
)
