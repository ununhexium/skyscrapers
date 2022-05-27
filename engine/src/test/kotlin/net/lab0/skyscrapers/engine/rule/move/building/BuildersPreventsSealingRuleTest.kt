package net.lab0.skyscrapers.engine.rule.move.building

import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.engine.rule.move.seal.BuildersPreventsSealingRule
import net.lab0.skyscrapers.api.structure.Position
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
