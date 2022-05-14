package net.lab0.skyscrapers.logic.rule.move.building

import net.lab0.skyscrapers.logic.api.Rule
import net.lab0.skyscrapers.logic.api.TurnType
import net.lab0.skyscrapers.logic.rule.AbstractRule

class BlocksAvailabilityRule :
  Rule<TurnType.MoveTurn.BuildTurn>,
  net.lab0.skyscrapers.logic.rule.AbstractRule<TurnType.MoveTurn.BuildTurn>(
    "Ensure there are enough blocks",
    "Prevent the player from playing a building action if there is not enough remaining blocks for that action",
    { state, turn ->
      val nextHeight = state.buildings[turn.build] + 1

      if (!state.blocks.hasBlock(nextHeight))
        "There is no block of height ${nextHeight.value} available anymore"
      else null
    }
  )