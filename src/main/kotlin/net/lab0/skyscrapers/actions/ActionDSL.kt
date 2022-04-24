package net.lab0.skyscrapers.actions

import net.lab0.skyscrapers.Game
import net.lab0.skyscrapers.exception.WrongPlayerTurn

class ActionDSL {
  fun player(player:Int, phase: PhaseDSL.() -> Action): Action {
    return { game ->
      if(game.currentPlayer != player)
        throw WrongPlayerTurn(player, game.currentPlayer)

      phase(PhaseDSL(player))(game)
    }
  }
}
