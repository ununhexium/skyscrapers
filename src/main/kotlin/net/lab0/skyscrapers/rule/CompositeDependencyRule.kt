package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.api.*

/**
 * The first rule depends on the second, which depends on the third and so on.
 * If the first rule failed, then the subsequent rules can't be checked.
 */
class CompositeDependencyRule<T>(val rules: List<Rule<T>>) : Rule<T> where T : Turn {
  override val name =
    "Composite rule of ${rules.joinToString(separator = ", ") { it.name }}"

  override val description =
    "Composite rule with descriptions \n${rules.joinToString(separator = ",\n") { it.description }}"

  override fun checkRule(
    state: GameState,
    turn: T
  ): List<GameRuleViolation> {
    val firstBreak =
      rules.firstOrNull() { it.checkRule(state, turn).isNotEmpty() }

    if (firstBreak != null) {
      return firstBreak.checkRule(state, turn)
    }

    return listOf()
  }
}
