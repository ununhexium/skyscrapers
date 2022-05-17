package net.lab0.skyscrapers.logic.rule.placement

import net.lab0.skyscrapers.logic.DefaultGames
import net.lab0.skyscrapers.logic.api.Game
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.GameRuleViolationImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CantGiveUpDuringPlacementRuleTest {
  @Test
  fun `can give up during movement phase`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = CantGiveUpDuringPlacementRule
    val turn = TurnType.GiveUpTurn(0)

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't give up during placement phase`() {
    val g = Game.new()
    val rule = CantGiveUpDuringPlacementRule
    val turn = TurnType.GiveUpTurn(0)

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(rule, "Can't give up during the placement phase")
      )
    )
  }

}