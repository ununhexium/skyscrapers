package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.api.*

class SealsPreventAnyMoveAndAction : Rule<TurnType.MoveTurn> {
  override val name = "Seals prevent building and moving"
  override val description = "Seals prevent moving, building and adding seals"

  override fun checkRule(
    state: GameState,
    turn: TurnType.MoveTurn
  ): List<GameRuleViolation> {
    return if (state.seals[turn.target]) {
      listOf(
        GameRuleViolationImpl(
          this,
          "Can't move to ${turn.target} because it is sealed"
        )
      )
    } else if (state.seals[turn.sealOrBuild]) {
      when (turn) {
        is TurnType.MoveTurn.MoveAndBuildTurn -> listOf(
          GameRuleViolationImpl(
            this,
            "Can't build at ${turn.build} because it is sealed"
          )
        )
        is TurnType.MoveTurn.MoveAndSealTurn -> listOf(
          GameRuleViolationImpl(
            this,
            "Can't seal at ${turn.seal} because it is already sealed"
          )
        )
      }
    } else listOf()
  }
}