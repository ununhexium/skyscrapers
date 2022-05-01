package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.api.GameRuleViolation
import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl

object BuilderPreventsBuildingRule : Rule<TurnType.MoveTurn.BuildTurn> {
  override val name = "Building location must be free of builders"
  override val description =
    "When building or sealing, the position where this happens must not have any builder"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn.BuildTurn
  ): List<GameRuleViolation> {
    if (state.builders[turn.sealOrBuild] != null) {
      return listOf(
        GameRuleViolationImpl(
          this,
          "Can't build at ${turn.sealOrBuild}: a builder is present"
        )
      )
    }
    return listOf()
  }
}
