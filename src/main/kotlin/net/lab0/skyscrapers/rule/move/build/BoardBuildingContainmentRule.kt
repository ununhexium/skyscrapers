package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.AbstractRule

object BoardBuildingContainmentRule : AbstractRule<TurnType.MoveTurn.BuildTurn>(
  "Board containment",
  "The building must be placed inside the board",
  { state: GameState,
    turn: TurnType.MoveTurn.BuildTurn ->

    if (!state.isWithinBounds(turn.build))
      "Can't build outside ${turn.build} of the board"
    else null
  }
)
