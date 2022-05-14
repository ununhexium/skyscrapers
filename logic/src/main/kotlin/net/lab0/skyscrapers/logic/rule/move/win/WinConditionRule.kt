package net.lab0.skyscrapers.logic.rule.move.win

import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.AbstractRule

object WinConditionRule : net.lab0.skyscrapers.logic.rule.AbstractRule<TurnType.MoveTurn.WinTurn>(
  "Check win condition",
  "Checks that the requested win is valid",
  { state: GameState, turn: TurnType.MoveTurn.WinTurn ->
    val requiredHeight = state.blocks.maxHeight()
    val actualHeight = state.buildings[turn.target]

    if (actualHeight < requiredHeight)
      "Winning requires to move to a cell of height ${requiredHeight.value}, your target location is of height ${actualHeight.value}"
    else null
  }
)
