package net.lab0.skyscrapers.rule.placement

import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.AbstractRule
import net.lab0.skyscrapers.structure.Phase

object CantGiveUpDuringPlacementRule : AbstractRule<TurnType>(
  "Can't give up during placement phase",
  "",
  { state: GameState, turn: TurnType ->
    if (turn is TurnType.GiveUpTurn && state.phase == Phase.PLACEMENT)
      "Can't give up during the placement phase"
    else null
  }
)
