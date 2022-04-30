package net.lab0.skyscrapers.rule.placement

import net.lab0.skyscrapers.GameImpl
import net.lab0.skyscrapers.api.Game
import net.lab0.skyscrapers.api.GameRuleViolationImpl
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlaceBuilderOnEmptyCellTest {
  @Test
  fun `can add a player on an empty cell`() {
    val g = Game.new()
    val rule = PlaceBuilderOnEmptyCell
    val turn = TurnType.PlacementTurn(0, Position(0, 0))

    assertThat(rule.checkRule(g.getState(), turn)).isEmpty()
  }

  @Test
  fun `can't add a player on a cell that already has a builder`() {
    val g = Game.new()
    val rule = PlaceBuilderOnEmptyCell
    val turn = TurnType.PlacementTurn(0, Position(0, 0))

    (g as GameImpl).backdoor.addBuilder(0, Position(0, 0))

    assertThat(rule.checkRule(g.getState(), turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't put a builder on a cell that already contains a builder at [0, 0]"
        )
      )
    )
  }

}