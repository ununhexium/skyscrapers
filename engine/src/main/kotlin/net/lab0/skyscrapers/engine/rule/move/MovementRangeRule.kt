package net.lab0.skyscrapers.engine.rule.move

import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.MoveOnly
import net.lab0.skyscrapers.engine.rule.AbstractRule

class MovementRangeRule : AbstractRule<MoveOnly>(
  "Movement limitation",
  "Checks that the builder doesn't go further than the tiles around it.",
  { _: GameState, turn: MoveOnly ->
    if (!turn.start.nextTo(turn.target))
      "The builder can't be moved from ${turn.start} to ${turn.target} because it's more than 1 tile away"
    else null
  }
)
