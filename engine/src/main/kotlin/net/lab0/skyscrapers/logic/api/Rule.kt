package net.lab0.skyscrapers.logic.api

import net.lab0.skyscrapers.logic.rule.AbstractRule
import net.lab0.skyscrapers.logic.rule.RuleWithDependencies

interface Rule<T> {
  val name: String
  val description: String

  fun checkRule(state: GameState, turn: T): List<GameRuleViolation>

  fun dependsOn(vararg other: Rule<T>): Rule<T> =
    RuleWithDependencies(this, *other)

}
