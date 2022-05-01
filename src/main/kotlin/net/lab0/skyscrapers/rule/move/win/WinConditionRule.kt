package net.lab0.skyscrapers.rule.move.win

import net.lab0.skyscrapers.api.GameRuleViolation
import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl

object WinConditionRule: Rule<TurnType.MoveTurn.WinTurn> {
  override val name = "Check win condition"
  override val description = "Checks that the requested win is valid"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn.WinTurn
  ): List<GameRuleViolation> {
    val requiredHeight = state.blocks.maxHeight()
    val actualHeight = state.buildings[turn.target]

    if(actualHeight < requiredHeight)
      return listOf(
        GameRuleViolationImpl(
          this,
          "Winning requires to move to a cell of height ${requiredHeight.value}, your target location is of height ${actualHeight.value}"
        )
      )

    return listOf()
  }
}
