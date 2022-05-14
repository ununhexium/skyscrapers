package net.lab0.skyscrapers.logic.rule

import net.lab0.skyscrapers.logic.api.GameRuleViolation
import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.Rule
import net.lab0.skyscrapers.logic.api.TurnType

data class FakeRule(
  val fails: Boolean,
  override val name: String = "Fake name",
  override val description: String = "Fake description",
) : Rule<TurnType> {
  companion object {
    fun violated(name: String = "") = FakeRule(true, name, name)
    fun abidden(name: String = "") = FakeRule(false, name, name)
  }

  override fun checkRule(state: GameState, turn: TurnType) =
    if (fails) listOf(GameRuleViolationImpl(this, "Fake detail"))
    else listOf()
}
