package net.lab0.skyscrapers.engine.rule

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType

/**
 * Only the player's whose turn it is to pay may do so.
 */
object CheckCurrentPlayer : AbstractRule<TurnType>(
  "Play in turns",
  "Checks that only the player that should be playing on the current turn is allowed to play",
  { state: GameState, turn: TurnType ->
    if (turn.player == state.currentPlayer) null
    else "Can't play now, it's player#${state.currentPlayer}'s turn"
  }
)