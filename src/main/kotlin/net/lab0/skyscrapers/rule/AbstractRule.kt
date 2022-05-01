package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.api.GameRuleViolation
import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.TurnType

abstract class AbstractRule<T>(
  override val name: String,
  override val description: String,
  val check: (state: GameState, turn: T) -> String?
) : Rule<T> {
  override fun checkRule(state: GameState, turn: T): List<GameRuleViolation> {
    return check(state, turn)
      ?.let { listOf(GameRuleViolationImpl(this, it)) }
      ?: listOf()
  }
}
