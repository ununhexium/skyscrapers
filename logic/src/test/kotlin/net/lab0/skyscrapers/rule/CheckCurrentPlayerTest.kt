package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.DefaultGames
import net.lab0.skyscrapers.api.TurnType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CheckCurrentPlayerTest {
  @Test
  fun `can play when it's the player's turn`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = CheckCurrentPlayer
    val turn = TurnType.GiveUpTurn(0)

    assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't play when it's not their turn`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = CheckCurrentPlayer
    val turn = TurnType.GiveUpTurn(1)

    assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(rule, "Can't play now, it's player#0's turn")
      )
    )
  }
}