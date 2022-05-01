package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState

/**
 * Limit the building range of the player
 */
class BuildingRangeRule : Rule<TurnType.MoveTurn.BuildTurn> {
  override val name = "Building range limit"
  override val description =
    "The player must build in the 8 cells around the moved builder"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn.BuildTurn,
  ): List<GameRuleViolation> {
    if (turn.target.nextTo(turn.build)) return listOf()

    return listOf(
      GameRuleViolationImpl(
        this,
        "Can't use the builder at ${turn.target} to build at ${turn.build}"
      )
    )
  }
}
