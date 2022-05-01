package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.AbstractRule

class BlocksAvailabilityRule :
  Rule<TurnType.MoveTurn.BuildTurn>,
  AbstractRule<TurnType.MoveTurn.BuildTurn>(
    "Ensure there are enough blocks",
    "Prevent the player from playing a building action if there is not enough remaining blocks for that action",
    { state, turn ->
      val nextHeight = state.buildings[turn.build] + 1

      if (!state.blocks.hasBlock(nextHeight))
        "There is no block of height ${nextHeight.value} available anymore"
      else null
    }
  )