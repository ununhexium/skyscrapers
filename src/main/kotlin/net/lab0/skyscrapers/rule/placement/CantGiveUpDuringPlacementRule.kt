package net.lab0.skyscrapers.rule.placement

import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.structure.Phase

object CantGiveUpDuringPlacementRule : Rule<TurnType> {
  override val name = "Can't give up during placement phase"
  override val description = ""

  override fun checkRule(
    state: GameState,
    turn: TurnType
  ) =
    if (turn is TurnType.GiveUpTurn && state.phase == Phase.PLACEMENT) listOf(
      GameRuleViolationImpl(this, "Can't give up during the placement phase")
    ) else listOf()
}
