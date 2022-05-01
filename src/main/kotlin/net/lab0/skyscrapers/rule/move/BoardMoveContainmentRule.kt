package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState

/**
 * The game happens inside the board only ;p
 */
object BoardMoveContainmentRule : Rule<TurnType.MoveTurn> {
  override val name = "Board containment"
  override val description = "The game is played inside the board only"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn
  ): List<GameRuleViolation> {
    if (!state.isWithinBounds(turn.start)) {
      return listOf(
        GameRuleViolationImpl(
          this,
          "Can't move from outside ${turn.start} the board"
        )
      )
    }

    if(!state.isWithinBounds(turn.target)){
      return listOf(
        GameRuleViolationImpl(
          this,
          "Can't move outside ${turn.target} of the board"
        )
      )
    }

    return listOf()
  }

}