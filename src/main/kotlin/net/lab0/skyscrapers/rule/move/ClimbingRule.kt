package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.GameRuleViolation
import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState

class ClimbingRule : Rule<TurnType.MoveTurn> {
  override val name = "Height progression"
  override val description =
    "Checks that the builder doesn't climb more than 1 step at a time"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn
  ): List<GameRuleViolation> {
    val targetHeight = state.buildings[turn.target]
    val startHeight = state.buildings[turn.start]
    val climb = targetHeight - startHeight

    if (climb > 1)
      return listOf(
        GameRuleViolationImpl(
          this,
          "Can't climb from ${turn.start} height=${startHeight.value} to ${turn.target} height = ${targetHeight.value}"
        )
      )

    return listOf()
  }
}