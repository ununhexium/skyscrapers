package net.lab0.skyscrapers.engine.rule.move.building

import net.lab0.skyscrapers.api.structure.GameRuleViolationImpl
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.GameImpl
import net.lab0.skyscrapers.engine.editor
import net.lab0.skyscrapers.engine.rule.move.seal.SealsPreventSealingRule
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

    (g as GameImpl).backdoor.forceState(g.state.editor().seal(seal))

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