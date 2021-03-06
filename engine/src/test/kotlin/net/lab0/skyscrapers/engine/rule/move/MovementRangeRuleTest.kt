package net.lab0.skyscrapers.engine.rule.move

import net.lab0.skyscrapers.api.structure.GameRuleViolationImpl
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.DefaultGames
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MovementRangeRuleTest {
  @Test
  fun `can move`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = MovementRangeRule()
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(0, 1),
      Position(0, 2)
    )

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't move move than 1 tile away`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = MovementRangeRule()
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(0, 2),
      Position(0, 1)
    )

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "The builder can't be moved from [0, 0] to [0, 2] because it's more than 1 tile away"
        )
      )
    )
  }

}