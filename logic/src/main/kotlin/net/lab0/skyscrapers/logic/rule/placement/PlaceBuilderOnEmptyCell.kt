package net.lab0.skyscrapers.logic.rule.placement

import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.AbstractRule

object PlaceBuilderOnEmptyCell : net.lab0.skyscrapers.logic.rule.AbstractRule<TurnType.PlacementTurn>(
  "Place builders on empty cells",
  "During the placement phase, builder must be placed on empty board cells.",
  { state: GameState, turn: TurnType.PlacementTurn ->
    if (state.builders[turn.position] != null)
      "Can't put a builder on a cell that already contains a builder at ${turn.position}"
    else null
  }
)
