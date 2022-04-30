package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.api.*

/**
 * Only the player's whose turn it is to pay may do so.
 */
object CheckCurrentPlayer : Rule<TurnType> {
  override val name = "Each player may play only when it's their turn"
  override val description =
    "Checks that only the player that shuold be play now is playing"

  override fun checkRule(
    state: GameState,
    turn: TurnType
  ) =
    if (turn.player == state.currentPlayer) listOf()
    else listOf(
      GameRuleViolationImpl(
        this,
        "Can't play now, it's player#${state.currentPlayer}'s turn"
      )
    )

}