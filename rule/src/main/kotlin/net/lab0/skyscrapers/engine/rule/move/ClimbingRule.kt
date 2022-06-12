package net.lab0.skyscrapers.engine.rule.move

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.MoveOnly
import net.lab0.skyscrapers.engine.rule.AbstractRule

class ClimbingRule : AbstractRule<MoveOnly>(
  "Height progression",
  "Checks that the builder doesn't climb more than 1 step at a time",
  { state: GameState, turn: MoveOnly ->

    val targetHeight = state.buildings[turn.target]
    val startHeight = state.buildings[turn.start]
    val climb = targetHeight - startHeight

    if (climb > 1)
      "Can't climb from ${turn.start} height=${startHeight.value} to ${turn.target} height = ${targetHeight.value}"
    else null
  }
)
