package net.lab0.skyscrapers.engine.rule

import net.lab0.skyscrapers.engine.api.GameRuleViolation
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.engine.api.Rule

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
