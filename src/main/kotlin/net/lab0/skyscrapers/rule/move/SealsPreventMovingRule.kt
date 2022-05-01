package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState

object SealsPreventMovingRule : Rule<TurnType.MoveTurn> {
  override val name = "Seals prevent movement"
  override val description = "Can't move a builder on a cell that contains a seal"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn
  ): List<GameRuleViolation> {
    if (state.seals[turn.target]) {
      return listOf(
        GameRuleViolationImpl(
          this,
          "Can't move to ${turn.target} because it is sealed"
        )
      )
    }

    return listOf()
  }
}