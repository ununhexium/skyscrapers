package net.lab0.skyscrapers.engine.rule

import net.lab0.skyscrapers.editor
import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.GameImpl
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.rule.move.SealsPreventMovingRule
import net.lab0.skyscrapers.engine.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SealsPreventMovingRuleTest {

  @Test
  fun `can move where there is no seal`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    val state = g.state
    val turn = TurnType.MoveTurn.SealTurn(
      0,
      Position(0, 0),
      Position(0, 0),
      Position(1, 1)
    )
    val rule = SealsPreventMovingRule

    assertThat(rule.checkRule(state, turn)).isEmpty()
  }

  @Test
  fun `prevent moving where there is a seal`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 1)

    (g as GameImpl).backdoor.forceState(g.state.editor().seal(target))

    val state = g.state
    val turn = TurnType.MoveTurn.SealTurn(
      0,
      Position(0, 0),
      target,
      Position(2, 2),
    )
    val rule = SealsPreventMovingRule

    assertThat(rule.checkRule(state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't move to [1, 1] because it is sealed"
        )
      )
    )
  }

}