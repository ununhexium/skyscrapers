package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.DefaultGames
import net.lab0.skyscrapers.GameImpl
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BuildersMoveToEmptyCellsTest {
  companion object {
    val rule = BuildersMoveToEmptyCells
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

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't move a builder that doesn't exist`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(1, 1),
      Position(1, 2),
      Position(2, 2)
    )

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "There is no builder at [1, 1]"
        )
      )
    )
  }

  @Test
  fun `can't move a builder on top of another builder`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 2)

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      target,
      Position(2, 2)
    )

    (g as GameImpl).backdoor.forceState(g.state.placeBuilder(0, target))

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "There is already a builder from player#0 at [1, 2]"
        )
      )
    )
  }

  @Test
  fun `can't move a builder on top of an opponent's builder`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 0)

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      target,
      Position(1, 1)
    )

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "There is already a builder from player#1 at [1, 0]"
        )
      )
    )
  }
}