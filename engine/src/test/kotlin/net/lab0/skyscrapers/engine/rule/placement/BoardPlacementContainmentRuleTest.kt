package net.lab0.skyscrapers.engine.rule.placement

import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.engine.structure.Position
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class BoardPlacementContainmentRuleTest {
  @Test
  fun `can place inside the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = BoardPlacementContainmentRule

    val turn = TurnType.PlacementTurn(g.state.currentPlayer, Position(0, 0))

    Assertions.assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't build outside of the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = BoardPlacementContainmentRule

    val turn = TurnType.PlacementTurn(g.state.currentPlayer, Position(0, -1))

    Assertions.assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't place outside [0, -1] of the board"
        )
      )
    )


    val turn2 = TurnType.PlacementTurn(g.state.currentPlayer, Position(5, 5))

    Assertions.assertThat(rule.checkRule(g.state, turn2)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't place outside [5, 5] of the board"
        )
      )
    )
  }
}