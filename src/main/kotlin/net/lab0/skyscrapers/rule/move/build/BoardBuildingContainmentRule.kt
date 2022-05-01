package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState

/**
 * The game happens inside the board only ;p
 */
// TODO: sealing containment
object BoardBuildingContainmentRule : Rule<TurnType.MoveTurn.BuildTurn> {
  override val name = "Board containment"
  override val description = "The game is played inside the board only"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn.BuildTurn
  ): List<GameRuleViolation> {

    if(!state.isWithinBounds(turn.build)) {
      return listOf(
        GameRuleViolationImpl(
          this,
          "Can't build outside ${turn.build} of the board"
        )
      )
    }

    return listOf()
  }

}