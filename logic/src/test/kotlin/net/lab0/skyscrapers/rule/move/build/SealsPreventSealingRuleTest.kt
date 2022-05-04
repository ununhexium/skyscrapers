package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.DefaultGames
import net.lab0.skyscrapers.GameImpl
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.rule.move.seal.SealsPreventSealingRule
import net.lab0.skyscrapers.structure.Position
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class SealsPreventSealingRuleTest{
  @Test
  fun `can move and seal where there is no seal`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    val state = g.state
    val turn = TurnType.MoveTurn.SealTurn(
      0,
      Position(0, 0),
      Position(0, 0),
      Position(1, 1)
    )
    val rule = SealsPreventSealingRule

    Assertions.assertThat(rule.checkRule(state, turn)).isEmpty()
  }

  @Test
  fun `prevent sealing where there is a seal`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 1)
    val seal = Position(2, 2)

    (g as GameImpl).backdoor.forceState(g.state.seal(seal))

    val state = g.state
    val turn = TurnType.MoveTurn.SealTurn(
      0,
      Position(0, 0),
      target,
      seal,
    )
    val rule = SealsPreventSealingRule

    Assertions.assertThat(rule.checkRule(state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't seal at [2, 2] because it is already sealed"
        )
      )
    )
  }


}