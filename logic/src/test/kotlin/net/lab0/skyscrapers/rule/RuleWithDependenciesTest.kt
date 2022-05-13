package net.lab0.skyscrapers.rule

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import net.lab0.skyscrapers.api.Game
import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.TurnType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RuleWithDependenciesTest {
  companion object {
    val rule = FakeRule.abidden()
    val state = Game.new().state
    val turn = TurnType.GiveUpTurn(0)
    val violation = GameRuleViolationImpl(rule, "")
  }

  @Test
  fun `don't evaluate the rule if a dependency is violated`() {
    val main = spyk(FakeRule.abidden("main"))
    val violated = spyk(FakeRule.violated("violated"))
    val ok = spyk(FakeRule.abidden("ok"))

    val dep = RuleWithDependencies(main, violated, ok)

    assertThat(dep.checkRule(state, turn).map { it.name })
      .isEqualTo(listOf("violated"))

    // the main rule was not invoked because of failed dependency
    verify(exactly = 0) {
      main.checkRule(state, turn)
    }
    verify(exactly = 1) {
      violated.checkRule(state, turn)
    }
  }

  @Test
  fun `evaluate the main rule if the other rules are abidden`() {
    val main = spyk(FakeRule.abidden("main"))
    val rule1 = spyk(FakeRule.abidden("rule1"))
    val rule2 = spyk(FakeRule.abidden("rule2"))

    val dep = RuleWithDependencies(main, rule1, rule2)

    assertThat(dep.checkRule(state, turn)).isEmpty()

    verify(exactly = 1) {
      listOf(main, rule1, rule2).forEach {
        it.checkRule(state, turn)
      }
    }
  }
}
