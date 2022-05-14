package net.lab0.skyscrapers.logic.rule.placement

import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.AbstractRule

object BoardPlacementContainmentRule : AbstractRule<TurnType.PlacementTurn>(
  "Board containment",
  "The builder must be placed inside the board",
  { state: GameState,
    turn: TurnType.PlacementTurn ->
    if (!state.isWithinBounds(turn.position))
      "Can't place outside ${turn.position} of the board"
    else null
  }
)
