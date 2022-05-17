package net.lab0.skyscrapers.logic.rule

import net.lab0.skyscrapers.logic.api.GameRuleViolation
import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.Rule

abstract class AbstractRule<T>(
  override val name: String,
  override val description: String,
  val check: (state: GameState, turn: T) -> String?
) : Rule<T> {
  override fun checkRule(state: GameState, turn: T): List<GameRuleViolation> {
    return check(state, turn)
      ?.let { listOf(
        GameRuleViolationImpl(
          this,
          it
        )
      ) }
      ?: listOf()
  }
}
