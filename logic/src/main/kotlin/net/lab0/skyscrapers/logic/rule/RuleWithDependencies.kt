package net.lab0.skyscrapers.logic.rule

import net.lab0.skyscrapers.logic.api.GameRuleViolation
import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.Rule

class RuleWithDependencies<T>(
  val mainRule: Rule<T>,
  val dependencies: List<Rule<T>>
) : Rule<T> {
  constructor(mainRule: Rule<T>, vararg dependencies: Rule<T>) :
      this(mainRule, dependencies.toList())

  override val name = mainRule.name
  override val description = mainRule.description

  override fun checkRule(state: GameState, turn: T) =
    dependencies
      .flatMap { it.checkRule(state, turn) }
      .ifEmpty { mainRule.checkRule(state, turn) }
}
