package net.lab0.skyscrapers.engine.rule

import net.lab0.skyscrapers.engine.api.GameRuleViolation
import net.lab0.skyscrapers.engine.api.GameState
import net.lab0.skyscrapers.engine.api.Move
import net.lab0.skyscrapers.engine.api.MoveOnly
import net.lab0.skyscrapers.engine.api.Rule
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.exception.GameRuleViolationException

class RuleBook(
  val turnRules: List<Rule<TurnType>>,
  val placementRules: List<Rule<TurnType.PlacementTurn>>,
  val moveOnlyRules: List<Rule<MoveOnly>>,
  val moveRules: List<Rule<Move>>,
  val buildRules: List<Rule<TurnType.MoveTurn.BuildTurn>>,
  val sealRules: List<Rule<TurnType.MoveTurn.SealTurn>>,
  val winRules: List<Rule<TurnType.MoveTurn.WinTurn>>,
) {
  fun tryToPlay(turn: TurnType, state: GameState): List<GameRuleViolation> {
    try {
      throwIfViolatedRule(turnRules, turn, state)

      when (turn) {
        is TurnType.PlacementTurn ->
          throwIfViolatedRule(placementRules, turn, state)

        is TurnType.GiveUpTurn -> {} // check nothing

        is TurnType.MoveTurn -> {
          throwIfViolatedRule(moveOnlyRules, turn, state)
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

  fun <T> checkRules(
    rules: List<Rule<T>>,
    turn: T,
    state: GameState,
  ): List<GameRuleViolation> =
    rules
      .asSequence()
      .map { it.checkRule(state, turn) }
      .firstOrNull { it.isNotEmpty() }
      ?: listOf()

  fun <T> throwIfViolatedRule(
    rules: List<Rule<T>>,
    turn: T,
    state: GameState,
  ) {
    val violatedRules = checkRules(rules, turn, state)

    if (violatedRules.isNotEmpty()) {
      throw GameRuleViolationException(violatedRules)
    }
  }

  fun canMove(
    turn: MoveOnly,
    state: GameState
  ) = checkRules(moveOnlyRules, turn, state).isEmpty()
}