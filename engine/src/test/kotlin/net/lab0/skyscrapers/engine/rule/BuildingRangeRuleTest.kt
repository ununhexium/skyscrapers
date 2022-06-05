package net.lab0.skyscrapers.engine.rule

import net.lab0.skyscrapers.api.structure.GameRuleViolationImpl
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.rule.move.building.BuildingRangeRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BuildingRangeRuleTest {
  @Test
  fun `the player can build in the 8 positions around where it moved`() {
    val turn = TurnType.MoveTurn.BuildTurn(
      0, Position(0, 0), Position(0, 1), Position(0, 0)
    )

    val game = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val state = game.state

    val rule = BuildingRangeRule()

    assertThat(rule.checkRule(state, turn)).isEmpty()
  }

  @Test
  fun `the player can't build further than 1 position away from its target position`() {
    val start = Position(0, 0)
    val target = Position(0, 1)
    val build = Position(0, 3)

    val turn = TurnType.MoveTurn.BuildTurn(
      0, start, target, build
    )

    val game = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val state = game.state

    val rule = BuildingRangeRule()

    assertThat(rule.checkRule(state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't use the builder at $target to build at $build"
        )
      )
    )
  }
}
