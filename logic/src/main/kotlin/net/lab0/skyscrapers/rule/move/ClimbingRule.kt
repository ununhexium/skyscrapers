package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.AbstractRule

class ClimbingRule : AbstractRule<TurnType.MoveTurn>(
  "Height progression",
  "Checks that the builder doesn't climb more than 1 step at a time",
  { state: GameState, turn: TurnType.MoveTurn ->

    val targetHeight = state.buildings[turn.target]
    val startHeight = state.buildings[turn.start]
    val climb = targetHeight - startHeight

    if (climb > 1)
      "Can't climb from ${turn.start} height=${startHeight.value} to ${turn.target} height = ${targetHeight.value}"
    else null
  }
)
