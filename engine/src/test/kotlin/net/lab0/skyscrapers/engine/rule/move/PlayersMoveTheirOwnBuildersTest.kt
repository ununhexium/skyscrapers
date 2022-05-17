package net.lab0.skyscrapers.engine.rule.move

import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.engine.structure.Position
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlayersMoveTheirOwnBuildersTest {
  companion object {
    val rule = PlayersMoveTheirOwnBuilders
  }

  @Test
  fun `can move a builder`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(1, 1),
      Position(2, 2)
    )

    Assertions.assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't move a builder that doesn't belong to the player`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(1, 0),
      Position(1, 2),
      Position(2, 2)
    )

    Assertions.assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "The builder at [1, 0] doesn't belong to player#0. It belongs to player#1"
        )
      )
    )
  }
}
