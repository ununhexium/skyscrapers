package net.lab0.skyscrapers.logic.rule

import net.lab0.skyscrapers.logic.DefaultGames
import net.lab0.skyscrapers.logic.api.Game
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PhaseRuleTest {
  @Test
  fun `can give up at any time`() {
    val rule = PhaseRule
    val turn = TurnType.GiveUpTurn(0)
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    assertThat(rule.checkRule(Game.new().state, turn)).isEmpty()
    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can place builders during the placement phase`() {
    val g = Game.new()

    val turn = TurnType.PlacementTurn(0, Position(0, 0))
    val rule = PhaseRule

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't place builders during the movement phase`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val turn = TurnType.PlacementTurn(0, Position(0, 0))
    val rule = PhaseRule

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't place a builder during the movement phase"
        )
      )
    )
  }

  @Test
  fun `can move builders during the movement phase`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val turn = TurnType.MoveTurn.SealTurn(
      0,
      Position(0, 0),
      Position(1, 0),
      Position(0, 0)
    )
    val rule = PhaseRule

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't move builders during the placement phase`() {
    val g = Game.new()
    val turn = TurnType.MoveTurn.SealTurn(
      0,
      Position(0, 0),
      Position(1, 0),
      Position(0, 0)
    )
    val rule = PhaseRule

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't move a builder during the placement phase"
        )
      )
    )
  }
}