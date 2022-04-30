package net.lab0.skyscrapers.rule.placement

import net.lab0.skyscrapers.api.*

object PlaceBuilderOnEmptyCell : Rule<TurnType.PlacementTurn> {
  override val name = "Place builders on empty cells"
  override val description =
    "During the placement phase, builder must be placed on empty board cells."

  override fun checkRule(
    state: GameState,
    turn: TurnType.PlacementTurn
  ): List<GameRuleViolation> {
    if (state.builders[turn.position] != null) return listOf(
      GameRuleViolationImpl(
        this,
        "Can't put a builder on a cell that already contains a builder at ${turn.position}"
      )
    )

    return listOf()
  }
}