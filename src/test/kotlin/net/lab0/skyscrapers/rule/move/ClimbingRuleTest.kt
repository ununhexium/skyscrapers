package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.DefaultGames
import net.lab0.skyscrapers.GameImpl
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ClimbingRuleTest {
  @Test
  fun `can climb up 1 step at a time`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 1)
    val turn = TurnType.MoveTurn.MoveAndBuildTurn(
      0,
      Position(0, 0),
      target,
      Position(2, 2)
    )
    val rule = ClimbingRule()

    (g as GameImpl).backdoor.setHeight(target, 1)

    assertThat(rule.checkRule(g.getState(), turn)).isEmpty()
  }

  @Test
  fun `can't climb up 2 steps at a time`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 1)
    val turn = TurnType.MoveTurn.MoveAndBuildTurn(
      0,
      Position(0, 0),
      target,
      Position(2, 2)
    )
    val rule = ClimbingRule()

    (g as GameImpl).backdoor.setHeight(target, 2)

    assertThat(rule.checkRule(g.getState(), turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't climb from [0, 0] height=0 to [1, 1] height = 2"
        )
      )
    )
  }

}