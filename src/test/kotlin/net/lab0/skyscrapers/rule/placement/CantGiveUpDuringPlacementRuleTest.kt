package net.lab0.skyscrapers.rule.placement

import net.lab0.skyscrapers.DefaultGames
import net.lab0.skyscrapers.api.Game
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CantGiveUpDuringPlacementRuleTest {
  @Test
  fun `can give up during movement phase`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = CantGiveUpDuringPlacementRule
    val turn = TurnType.GiveUpTurn(0)

    assertThat(rule.checkRule(g.getState(), turn)).isEmpty()
  }

  @Test
  fun `can't give up during placement phase`() {
    val g = Game.new()
    val rule = CantGiveUpDuringPlacementRule
    val turn = TurnType.GiveUpTurn(0)

    assertThat(rule.checkRule(g.getState(), turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(rule, "Can't give up during the placement phase")
      )
    )
  }

}