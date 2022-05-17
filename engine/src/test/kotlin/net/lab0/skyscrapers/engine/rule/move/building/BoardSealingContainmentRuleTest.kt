package net.lab0.skyscrapers.engine.rule.move.building

import net.lab0.skyscrapers.engine.DefaultGames
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.engine.rule.move.seal.BoardSealingContainmentRule
import net.lab0.skyscrapers.engine.structure.Position
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class BoardSealingContainmentRuleTest{
  @Test
  fun `can seal inside the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()
    val rule = BoardSealingContainmentRule

    val turn = TurnType.MoveTurn.SealTurn(
      0,
      Position(0, 0),
      Position(1, 1),
      Position(2, 2)
    )

    Assertions.assertThat(rule.checkRule(g.state, turn)).isEmpty()
  }

  @Test
  fun `can't seal outside of the board`() {
    val g = DefaultGames.newGameWithSequentiallyPlacedBuilders()

    val turn = TurnType.MoveTurn.SealTurn(
      0,
      Position(0, 0),
      Position(1, 0),
      Position(1, -1)
    )

    val rule = BoardSealingContainmentRule

    Assertions.assertThat(rule.checkRule(g.state, turn)).isEqualTo(
      listOf(
        GameRuleViolationImpl(
          rule,
          "Can't seal outside [1, -1] of the board"
        )
      )
    )
  }

}