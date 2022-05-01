package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.DefaultGames
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.structure.Position
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class BoardBuildingContainmentRuleTest{
  @Test
  fun `can build inside the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = BoardBuildingContainmentRule

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(1, 1),
      Position(2, 2)
    )

    Assertions.assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't build outside of the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    val turn = TurnType.MoveTurn.BuildTurn(
      0,
      Position(0, 0),
      Position(1, 0),
      Position(1, -1)
    )

    val rule = BoardBuildingContainmentRule

    Assertions.assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't build outside [1, -1] of the board"
        )
      )
    )
  }
}