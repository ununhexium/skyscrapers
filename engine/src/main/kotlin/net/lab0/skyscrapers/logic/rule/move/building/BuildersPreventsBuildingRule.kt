package net.lab0.skyscrapers.logic.rule.move.building

import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.AbstractRule

object BuildersPreventsBuildingRule : AbstractRule<TurnType.MoveTurn.BuildTurn>(
  "Building location must be free of builders",
  "When building, the position where building happens must not have any builder",
  { state: GameState, turn: TurnType.MoveTurn.BuildTurn ->
    if (state.builders[turn.build] != null)
      "Can't build at ${turn.build}: a builder is present"
    else null
  }
)
