package net.lab0.skyscrapers.api.structure

data class GameRuleViolationImpl(
  override val name: String,
  override val description: String,
  override val detail: String,
) : GameRuleViolation {
  constructor (rule: Rule<*>, detail: String) :
      this(rule.name, rule.description, detail)
}
