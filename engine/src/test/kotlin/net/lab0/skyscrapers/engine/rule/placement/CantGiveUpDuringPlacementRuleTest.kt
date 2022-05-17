package net.lab0.skyscrapers.engine.rule.placement

import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.rule.GameRuleViolationImpl
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