package net.lab0.skyscrapers.logic.rule.move.building

import net.lab0.skyscrapers.logic.DefaultGames
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.logic.rule.move.seal.BuildersPreventsSealingRule
import net.lab0.skyscrapers.logic.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BuildersPreventsSealingRuleTest {
  @Test
  fun `can seal where there is no builder`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val turn = TurnType.MoveTurn.SealTurn(
      0,
      Position(0, 0),
      Position(0, 0),
      Position(1, 1)
    )
    val rule = BuildersPreventsSealingRule

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't seal where there is a builder`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val turn = TurnType.MoveTurn.SealTurn(
      0,
      Position(0, 0),
      Position(0, 0),
      Position(1, 0)
    )
    val rule = BuildersPreventsSealingRule

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(rule, "Can't seal at [1, 0]: a builder is present")
      )
    )
  }
}
