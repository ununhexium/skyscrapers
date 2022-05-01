package net.lab0.skyscrapers.rule.move

import net.lab0.skyscrapers.api.*
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState

object DefaultBuildersMovementRule : Rule<TurnType.MoveTurn> {
  override val name = "Check builders are moved"
  override val description =
    "Enforces that the player moves an existing builder of their own and that it's moved to an empty cell"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn
  ): List<GameRuleViolation> {
    if (state.builders[turn.start] == null)
      return listOf(
        GameRuleViolationImpl(
          this,
          "There is no builder that belongs to player#${turn.player} at ${turn.start}"
        )
      )

    if (state.builders[turn.start] != turn.player) {
      return listOf(
        GameRuleViolationImpl(
          this,
          "The builder at ${turn.start} doesn't belong to player#${turn.player}. It belongs to player#${state.builders[turn.start]}"
        )
      )
    }

    if (state.builders[turn.target] != null)
      return listOf(
        GameRuleViolationImpl(
          this,
          "There is already a builder from player#${turn.player} at ${turn.target}"
        )
      )

    if (turn.start == turn.target)
      return listOf(
        GameRuleViolationImpl(
          this,
          "The builder at ${turn.start} didn't move"
        )
      )

    return listOf()
  }
}
