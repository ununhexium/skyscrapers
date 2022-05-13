package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.api.GameRuleViolation
import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.Rule

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
