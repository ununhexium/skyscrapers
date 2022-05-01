package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.api.GameRuleViolation
import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState

object BuildersPreventsSealingRule : Rule<TurnType.MoveTurn.SealTurn> {
  override val name = "Building location must be free of builders"
  override val description =
    "When building or sealing, the position where this happens must not have any builder"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn.SealTurn
  ): List<GameRuleViolation> {
    if (state.builders[turn.seal] != null) {
      return listOf(
        GameRuleViolationImpl(
          this,
          "Can't seal at ${turn.seal}: a builder is present"
        )
      )
    }
    return listOf()
  }
}
