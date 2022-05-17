package net.lab0.skyscrapers.engine.rule.placement

import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.rule.AbstractRule

object PlaceBuilderOnEmptyCell : AbstractRule<TurnType.PlacementTurn>(
  "Place builders on empty cells",
  "During the placement phase, builder must be placed on empty board cells.",
  { state: GameState, turn: TurnType.PlacementTurn ->
    if (state.builders[turn.position] != null)
      "Can't put a builder on a cell that already contains a builder at ${turn.position}"
    else null
  }
)
