package net.lab0.skyscrapers.engine.rule

import net.lab0.skyscrapers.api.structure.GameRuleViolation
import net.lab0.skyscrapers.engine.api.Rule

data class GameRuleViolationImpl(
  override val name: String,
  override val description: String,
  override val detail: String,
) : GameRuleViolation {
  constructor (rule: Rule<*>, detail: String) :
      this(rule.name, rule.description, detail)
}
