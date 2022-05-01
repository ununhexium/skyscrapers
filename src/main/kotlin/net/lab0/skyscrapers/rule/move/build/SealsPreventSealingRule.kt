package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState

object SealsPreventSealingRule : Rule<TurnType.MoveTurn.SealTurn> {
  override val name = "Seals prevent sealing"
  override val description = ""

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn.SealTurn
  ): List<GameRuleViolation> {
    if (state.seals[turn.seal]) {
      return listOf(
        GameRuleViolationImpl(
          this,
          "Can't seal at ${turn.seal} because it is already sealed"
        )
      )
    }

    return listOf()
  }
}