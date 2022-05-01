package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.AbstractRule

class BuildingRangeRule : AbstractRule<TurnType.MoveTurn.BuildTurn>(
  "Building range limit",
  "The player must build in the 8 positions around the moved builder",
  { _: GameState,
    turn: TurnType.MoveTurn.BuildTurn ->
    if (turn.target.nextTo(turn.build)) null
    else "Can't use the builder at ${turn.target} to build at ${turn.build}"
  }
)
