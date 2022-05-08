package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.DefaultGames
import net.lab0.skyscrapers.GameImpl
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DefaultBuildersMovementRuleTest {
  @Test
  fun `can move a builder`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = DefaultBuildersMovementRule
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
    val rule = DefaultBuildersMovementRule
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
          "There is no builder that belongs to player#0 at [1, 1]"
        )
      )
    )
  }

  @Test
  fun `can't move a builder on top of another builder`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = DefaultBuildersMovementRule
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
  fun `can't move a builder that doesn't belong to the player`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = DefaultBuildersMovementRule

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(1, 0),
      Position(1, 2),
      Position(2, 2)
    )

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "The builder at [1, 0] doesn't belong to player#0. It belongs to player#1"
        )
      )
    )
  }
}