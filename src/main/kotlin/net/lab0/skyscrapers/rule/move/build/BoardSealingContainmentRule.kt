package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState

/**
 * The game happens inside the board only ;p
 */
// TODO: sealing containment
object BoardSealingContainmentRule : Rule<TurnType.MoveTurn.SealTurn> {
  override val name = "Board containment"
  override val description = "The game is played inside the board only"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn.SealTurn
  ): List<GameRuleViolation> {

    if(!state.isWithinBounds(turn.seal)) {
      return listOf(
        GameRuleViolationImpl(
          this,
          "Can't seal outside ${turn.seal} of the board"
        )
      )
    }

    return listOf()
  }

}