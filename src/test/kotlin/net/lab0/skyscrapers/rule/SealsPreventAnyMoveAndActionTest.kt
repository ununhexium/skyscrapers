package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.DefaultGames
import net.lab0.skyscrapers.GameImpl
import net.lab0.skyscrapers.api.GameRuleViolationImpl
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SealsPreventAnyMoveAndActionTest {
  @Test
  fun `can move and build where there is no seal`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    val state = g.getState()
    val turn = TurnType.MoveTurn.MoveAndSealTurn(
      0,
      Position(0, 0),
      Position(0, 0),
      Position(1, 1)
    )
    val rule = SealsPreventAnyMoveAndAction()

    assertThat(rule.checkRule(state, turn)).isEmpty()
  }

  @Test
  fun `prevent movement where there is a seal`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 1)

    (g as GameImpl).backdoor.addSeal(target)

    val state = g.getState()
    val turn = TurnType.MoveTurn.MoveAndSealTurn(
      0,
      Position(0, 0),
      target,
      Position(2, 2),
    )
    val rule = SealsPreventAnyMoveAndAction()

    assertThat(rule.checkRule(state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't move to [1, 1] because it is sealed"
        )
      )
    )
  }

  @Test
  fun `prevent building where there is a seal`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 1)
    val build = Position(2, 2)

    (g as GameImpl).backdoor.addSeal(build)

    val state = g.getState()
    val turn = TurnType.MoveTurn.MoveAndBuildTurn(
      0,
      Position(0, 0),
      target,
      build,
    )
    val rule = SealsPreventAnyMoveAndAction()

    assertThat(rule.checkRule(state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't build at [2, 2] because it is sealed"
        )
      )
    )
  }

  @Test
  fun `prevent sealing where there is a seal`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 1)
    val seal = Position(2, 2)

    (g as GameImpl).backdoor.addSeal(seal)

    val state = g.getState()
    val turn = TurnType.MoveTurn.MoveAndSealTurn(
      0,
      Position(0, 0),
      target,
      seal,
    )
    val rule = SealsPreventAnyMoveAndAction()

    assertThat(rule.checkRule(state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't seal at [2, 2] because it is already sealed"
        )
      )
    )
  }


}