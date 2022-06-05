package net.lab0.skyscrapers.engine.rule.move.building

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.rule.GameRuleViolationImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BuildersPreventsBuildingRuleTest {
  @Test
  fun `can build where there is no builder`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(0, 0),
      Position(1, 1)
    )
    val rule = BuildersPreventsBuildingRule

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't build where there is a builder`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(0, 0),
      Position(1, 0)
    )
    val rule = BuildersPreventsBuildingRule

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(rule, "Can't build at [1, 0]: a builder is present")
      )
    )
  }
}
