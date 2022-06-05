package net.lab0.skyscrapers.engine.rule.move.building

import net.lab0.skyscrapers.api.structure.BlocksData
import net.lab0.skyscrapers.api.structure.Height
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.editor
import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.GameImpl
import net.lab0.skyscrapers.api.structure.GameRuleViolationImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BlocksAvailabilityRuleTest {
  @Test
  fun `can build`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(1, 1),
      Position(2, 2)
    )
    val rule = BlocksAvailabilityRule()

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't build when there is no block available`() {
    val g =
      DefaultGames.newGameWithSequentiallyPlacedBuilders(
        blocks = BlocksData(Height(0) to 1, Height(1) to 1)
      )

    val build = Position(2, 2)

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(1, 1),
      build,
    )

    val rule = BlocksAvailabilityRule()

    (g as GameImpl).backdoor.forceState(g.state.editor().height(build, 1))

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "There is no block of height 2 available anymore"
        )
      )
    )
  }

}