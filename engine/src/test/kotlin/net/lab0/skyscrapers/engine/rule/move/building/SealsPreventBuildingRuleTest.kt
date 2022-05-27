package net.lab0.skyscrapers.engine.rule.move.building

import net.lab0.skyscrapers.editor
import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.GameImpl
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.structure.Position
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class SealsPreventBuildingRuleTest {
  @Test
  fun `can build where there is no seal`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    val state = g.state
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(0, 0),
      Position(1, 1)
    )
    val rule = SealsPreventBuildingRule

    Assertions.assertThat(rule.checkRule(state, turn)).isEmpty()
  }

  @Test
  fun `prevent building where there is a seal`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val target = Position(1, 1)
    val build = Position(2, 2)

    (g as GameImpl).backdoor.forceState(g.state.editor().seal(build))

    val state = g.state
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      target,
      build,
    )
    val rule = SealsPreventBuildingRule

    Assertions.assertThat(rule.checkRule(state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't build at [2, 2] because it is sealed"
        )
      )
    )
  }
}