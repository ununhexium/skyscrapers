package net.lab0.skyscrapers.rule.move.build

import net.lab0.skyscrapers.api.GameRuleViolation
import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.rule.GameRuleViolationImpl
import net.lab0.skyscrapers.api.GameState

class BlocksAvailabilityRule : Rule<TurnType.MoveTurn.BuildTurn> {
  override val name = "Ensure there are enough blocks"
  override val description =
    "Prevent the player from playing a building action if there is not enough remaining blocks for that action"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn.BuildTurn
  ): List<GameRuleViolation> {
    val nextHeight = state.buildings[turn.build] + 1
    if (!state.blocks.hasBlock(nextHeight))
      return listOf(
        GameRuleViolationImpl(
          this,
          "There is no block of height ${nextHeight.value} available anymore"
        )
      )

    return listOf()
  }

}