package net.lab0.skyscrapers.rule

import net.lab0.skyscrapers.api.GameRuleViolation
import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.Rule
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.exception.GameRuleViolationException
import net.lab0.skyscrapers.rule.move.*
import net.lab0.skyscrapers.rule.move.build.*
import net.lab0.skyscrapers.rule.move.seal.BoardSealingContainmentRule
import net.lab0.skyscrapers.rule.move.seal.BuildersPreventsSealingRule
import net.lab0.skyscrapers.rule.move.seal.SealingRangeRule
import net.lab0.skyscrapers.rule.move.seal.SealsPreventSealingRule
import net.lab0.skyscrapers.rule.move.win.WinConditionRule
import net.lab0.skyscrapers.rule.placement.CantGiveUpDuringPlacementRule
import net.lab0.skyscrapers.rule.placement.PlaceBuilderOnEmptyCell

class RuleBook(
  val turnRules: List<Rule<TurnType>> = listOf(
    PhaseRule,
    CheckCurrentPlayer,
    CantGiveUpDuringPlacementRule,
  ),

  val placementRules: List<Rule<TurnType.PlacementTurn>> = listOf(
    PlaceBuilderOnEmptyCell,
  ),

  val moveRules: List<Rule<TurnType.MoveTurn>> = listOf(
    BoardMoveContainmentRule,
    DefaultBuildersMovementRule,
    MovementRangeRule(),
    ClimbingRule(),
    SealsPreventMovingRule,
  ),

  val buildRules: List<Rule<TurnType.MoveTurn.BuildTurn>> = listOf(
    BoardBuildingContainmentRule,
    BlocksAvailabilityRule(),
    BuildingRangeRule(),
    BuildersPreventsBuildingRule,
    SealsPreventBuildingRule,
  ),

  val sealRules: List<Rule<TurnType.MoveTurn.SealTurn>> = listOf(
    BoardSealingContainmentRule,
    SealingRangeRule(),
    SealsPreventSealingRule,
    BuildersPreventsSealingRule,
  ),

  val winRules: List<Rule<TurnType.MoveTurn.WinTurn>> = listOf(
    WinConditionRule,
  ),
) {
  fun tryToPlay(turn: TurnType, state: GameState): List<GameRuleViolation> {
    try {
      throwIfViolatedRule(turnRules, turn, state)

      when (turn) {
        is TurnType.PlacementTurn ->
          throwIfViolatedRule(placementRules, turn, state)

        is TurnType.GiveUpTurn -> {} // check nothing

        is TurnType.MoveTurn -> {
          throwIfViolatedRule(moveRules, turn, state)

          val nextBuilderState = state.move(turn)

          when (turn) {
            is TurnType.MoveTurn.BuildTurn ->
              throwIfViolatedRule(buildRules, turn, nextBuilderState)

            is TurnType.MoveTurn.SealTurn ->
              throwIfViolatedRule(sealRules, turn, nextBuilderState)

            is TurnType.MoveTurn.WinTurn ->
              throwIfViolatedRule(winRules, turn, nextBuilderState)

          }
        }
      }
    } catch (violation: GameRuleViolationException) {
      return violation.violation
    }

    return listOf()
  }

  inline fun <reified T> throwIfViolatedRule(
    rules: List<Rule<T>>,
    turn: T,
    state: GameState,
  ) where T : TurnType {
    val violatedRule = rules.firstOrNull {
      it.checkRule(state, turn).isNotEmpty()
    }

    if (violatedRule != null) {
      throw GameRuleViolationException(
        violatedRule.checkRule(state, turn)
      )
    }
  }

}