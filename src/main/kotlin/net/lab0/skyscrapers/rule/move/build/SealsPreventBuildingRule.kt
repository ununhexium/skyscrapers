package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState

object SealsPreventBuildingRule : Rule<TurnType.MoveTurn.BuildTurn> {
  override val name = "Seals prevent building"
  override val description = ""

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn.BuildTurn
  ): List<GameRuleViolation> {
    if (state.seals[turn.build]) {
      return listOf(
        GameRuleViolationImpl(
          this,
          "Can't build at ${turn.build} because it is sealed"
        )
      )
    }

    return listOf()
  }
}