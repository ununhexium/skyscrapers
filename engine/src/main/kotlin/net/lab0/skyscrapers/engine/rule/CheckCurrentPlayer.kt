package net.lab0.skyscrapers.engine.rule

import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.TurnType

/**
 * Only the player's whose turn it is to pay may do so.
 */
object CheckCurrentPlayer : AbstractRule<TurnType>(
  "Each player may play only when it's their turn",
  "Checks that only the player that shuold be play now is playing",
  { state: GameState, turn: TurnType ->
    if (turn.player == state.currentPlayer) null
    else "Can't play now, it's player#${state.currentPlayer}'s turn"
  }
)