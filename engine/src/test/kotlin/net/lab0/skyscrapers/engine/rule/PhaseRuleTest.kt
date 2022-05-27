package net.lab0.skyscrapers.engine.rule

import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.GameFactoryImpl
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.api.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PhaseRuleTest {
  @Test
  fun `can give up at any time`() {
    val rule = PhaseRule
    val turn = TurnType.GiveUpTurn(0)
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    assertThat(rule.checkRule(GameFactoryImpl().new().state, turn)).isEmpty()
    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can place builders during the placement phase`() {
    val g = GameFactoryImpl().new()

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
    val g = GameFactoryImpl().new()
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
