package net.lab0.skyscrapers.logic.rule.move.building

import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.AbstractRule

object SealsPreventBuildingRule : AbstractRule<TurnType.MoveTurn.BuildTurn>(
  "Seals prevent building",
  "It's not possible to build on a sealed position",
  { state: GameState, turn: TurnType.MoveTurn.BuildTurn ->
    if (state.seals[turn.build])
      "Can't build at ${turn.build} because it is sealed"
    else null
  }
)