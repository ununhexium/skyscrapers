package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Game

class ActionDSL {
  fun player(player:Int, phase: PhaseDSL.() -> (Game) -> Unit): (Game) -> Unit {
    return { game ->
      phase(PhaseDSL(player))(game)
    }
  }
}
