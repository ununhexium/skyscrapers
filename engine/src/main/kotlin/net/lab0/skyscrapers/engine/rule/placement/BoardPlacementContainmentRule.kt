package net.lab0.skyscrapers.engine.rule.placement

import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.rule.AbstractRule

object BoardPlacementContainmentRule : AbstractRule<TurnType.PlacementTurn>(
  "Board containment",
  "The builder must be placed inside the board",
  { state: GameState,
    turn: TurnType.PlacementTurn ->
    if (turn.position !in state.bounds)
      "Can't place outside ${turn.position} of the board"
    else null
  }
)
