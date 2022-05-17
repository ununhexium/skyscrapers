package net.lab0.skyscrapers.engine.rule.placement

import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.rule.AbstractRule
import net.lab0.skyscrapers.engine.structure.Phase

object CantGiveUpDuringPlacementRule : AbstractRule<TurnType>(
  "Can't give up during placement phase",
  "",
  { state: GameState, turn: TurnType ->
    if (turn is TurnType.GiveUpTurn && state.phase == Phase.PLACEMENT)
      "Can't give up during the placement phase"
    else null
  }
)
