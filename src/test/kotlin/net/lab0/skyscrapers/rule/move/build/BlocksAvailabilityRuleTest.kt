package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.DefaultGames
import net.lab0.skyscrapers.GameImpl
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.structure.Height
import net.lab0.skyscrapers.structure.Position
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

    assertThat(rule.checkRule(g.getState(), turn)).isEmpty()
  }

  @Test
  fun `can't build when there is no block available`() {
    val g =
      DefaultGames.newGameWithSequentiallyPlacedBuilders(
        blocks = mapOf(Height(1) to 1)
      )

    val build = Position(2, 2)
    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(1, 1),
      build,
    )

    val rule = BlocksAvailabilityRule()

    (g as GameImpl).backdoor.setHeight(build, 1)

    assertThat(rule.checkRule(g.getState(), turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "There is no block of height 2 available anymore"
        )
      )
    )
  }

}