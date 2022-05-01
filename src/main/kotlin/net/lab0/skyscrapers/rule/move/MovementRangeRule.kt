package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.GameRuleViolation
import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.structure.GameState

class MovementRangeRule : Rule<TurnType.MoveTurn> {
  override val name = "Movement limitation"
  override val description =
    "Checks that the builder doesn't go further than the tiles around it."

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn
  ): List<GameRuleViolation> {
    if (!turn.start.nextTo(turn.target))
      return listOf(
        GameRuleViolationImpl(
          this,
          "The builder can't be moved from ${turn.start} to ${turn.target} because it's more than 1 tile away"
        )
      )

    return listOf()
  }
}