package net.lab0.skyscrapers.engine.rule.move

import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.editor
import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.GameImpl
import net.lab0.skyscrapers.api.structure.GameRuleViolationImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ClimbingRuleTest {
  @Test
  fun `can climb up 1 step at a time`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 1)
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      target,
      Position(2, 2)
    )
    val rule = ClimbingRule()

    (g as GameImpl).backdoor.forceState(g.state.editor().height(target, 1))

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't climb up 2 steps at a time`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 1)
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      target,
      Position(2, 2)
    )
    val rule = ClimbingRule()

    (g as GameImpl).backdoor.forceState(g.state.editor().height(target, 2))

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't climb from [0, 0] height=0 to [1, 1] height = 2"
        )
      )
    )
  }

}