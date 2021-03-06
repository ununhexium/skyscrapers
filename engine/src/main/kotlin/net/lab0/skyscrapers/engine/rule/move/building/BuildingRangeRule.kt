package net.lab0.skyscrapers.engine.rule.move.building

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.AbstractRule

class BuildingRangeRule : AbstractRule<TurnType.MoveTurn.BuildTurn>(
  "Building range limit",
  "The player must build in the 8 positions around the moved builder",
  { _: GameState,
    turn: TurnType.MoveTurn.BuildTurn ->
    if (turn.target.nextTo(turn.build)) null
    else "Can't use the builder at ${turn.target} to build at ${turn.build}"
  }
)
