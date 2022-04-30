package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.DefaultGames
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.move.BoardMoveContainmentRule
import net.lab0.skyscrapers.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BoardMoveContainmentRuleTest {
  @Test
  fun `can play inside the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = BoardMoveContainmentRule

    g.getState()

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(1, 1),
      Position(2, 2)
    )

    assertThat(rule.checkRule(g.getState(), turn)).isEmpty()
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

    assertThat(rule.checkRule(g.getState(), turn)).isEqualTo(
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

    assertThat(rule.checkRule(g.getState(), turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't move outside [-1, -1] of the board"
        )
      )
    )
  }

  @Test
  fun `can't build outside of the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(1, 0),
      Position(1, -1)
    )

    val rule = BoardMoveContainmentRule

    assertThat(rule.checkRule(g.getState(), turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't build outside [1, -1] of the board"
        )
      )
    )
  }

  @Test
  fun `can't seal outside of the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    val turn = TurnType.MoveTurn.SealTurn(
      0,
      Position(0, 0),
      Position(1, 0),
      Position(1, -1)
    )

    val rule = BoardMoveContainmentRule

    assertThat(rule.checkRule(g.getState(), turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't seal outside [1, -1] of the board"
        )
      )
    )
  }

}