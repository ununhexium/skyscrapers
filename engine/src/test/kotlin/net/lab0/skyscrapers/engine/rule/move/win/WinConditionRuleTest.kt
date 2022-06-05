package net.lab0.skyscrapers.engine.rule.move.win

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.editor
import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.GameImpl
import net.lab0.skyscrapers.api.structure.GameRuleViolationImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class WinConditionRuleTest {
  @Test
  fun `can win`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    (g as GameImpl).backdoor.forceState(
      g.state.editor().height(
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
      g.state.editor().height(
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