package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.exception.WrongPlayerTurn

class PlayerDSL {
  fun player(player:Int, phase: PhaseDSL.() -> Action): Action {
    return { game ->
      phase(PhaseDSL(player))(game)
    }
  }
}
