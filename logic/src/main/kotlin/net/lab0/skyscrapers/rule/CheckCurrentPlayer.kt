package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.api.GameState

/**
 * Only the player's whose turn it is to pay may do so.
 */
object CheckCurrentPlayer : AbstractRule<TurnType>(
  "Each player may play only when it's their turn",
  "Checks that only the player that shuold be play now is playing",
  { state: GameState, turn: TurnType ->
    if (turn.player == state.currentPlayer) null
    else
      "Can't play now, it's player#${state.currentPlayer}'s turn"
  }
)