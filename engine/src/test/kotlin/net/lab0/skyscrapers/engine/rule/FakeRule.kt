package net.lab0.skyscrapers.engine.rule

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.api.Rule

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
