package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.api.*

class BuildingRangeRule : Rule<Move> {
  override val name = "Building range limit"
  override val description =
    "The player must build in the 8 cells around the moved builder"

  override fun checkRule(
    game: GameState,
    turn: Move,
  ): List<GameRuleViolation> {
    if (turn.target.nextTo(turn.sealOrBuild)) return listOf()

    return listOf(
      GameRuleViolationImpl(
        this,
        "Can't use the builder at ${turn.target} to build at ${turn.sealOrBuild}"
      )
    )
  }
}
