package net.lab0.skyscrapers.engine.rule

import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.rule.move.BoardMoveContainmentRule
import net.lab0.skyscrapers.engine.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BoardMoveContainmentRuleTest {
  @Test
  fun `can play inside the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = BoardMoveContainmentRule

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(1, 1),
      Position(2, 2)
    )

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't move from outside the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(-1, -1),
      Position(0, 0),
      Position(1, 1)
    )

    val rule = BoardMoveContainmentRule

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't move from outside [-1, -1] the board"
        )
      )
    )
  }

  @Test
  fun `can't move outside of the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(-1, -1),
      Position(0, 0)
    )

    val rule = BoardMoveContainmentRule

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't move outside [-1, -1] of the board"
        )
      )
    )
  }

}