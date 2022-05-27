package net.lab0.skyscrapers.engine.rule.move

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.MoveOnly
import net.lab0.skyscrapers.engine.rule.AbstractRule

object SealsPreventMovingRule : AbstractRule<MoveOnly>(
  "Seals prevent movement",
  "Can't move a builder on a position that contains a seal",
  { state: GameState, turn: MoveOnly ->
    if (state.seals[turn.target])
      "Can't move to ${turn.target} because it is sealed"
    else null
  }
)
