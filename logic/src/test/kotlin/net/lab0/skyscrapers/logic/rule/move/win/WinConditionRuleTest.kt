package net.lab0.skyscrapers.logic.rule.move.win

import net.lab0.skyscrapers.logic.DefaultGames
import net.lab0.skyscrapers.logic.GameImpl
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.logic.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class WinConditionRuleTest {
  @Test
  fun `can win`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    (g as GameImpl).backdoor.forceState(
      g.state.height(
        Position(1, 1),
        g.state.blocks.maxHeight().value
      )
    )

    val turn = TurnType.MoveTurn.WinTurn(0, Position(0, 0), Position(1, 1))

    val rule = WinConditionRule

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't win just because you asked`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    (g as GameImpl).backdoor.forceState(
      g.state.height(
        Position(1, 1),
        2
      )
    )

    // the target position is not high enough
    val turn = TurnType.MoveTurn.WinTurn(0, Position(0, 0), Position(1, 1))

    val rule = WinConditionRule

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Winning requires to move to a cell of height 3, your target location is of height 2"
        )
      )
    )
  }
}