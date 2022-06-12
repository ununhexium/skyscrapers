package net.lab0.skyscrapers.engine.rule

import net.lab0.skyscrapers.api.structure.GameRuleViolation
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Move
import net.lab0.skyscrapers.api.structure.MoveOnly
import net.lab0.skyscrapers.api.structure.Rule
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.api.structure.TurnType.MoveTurn.BuildTurn
import net.lab0.skyscrapers.api.structure.TurnType.MoveTurn.SealTurn
import net.lab0.skyscrapers.api.structure.TurnType.MoveTurn.WinTurn
import net.lab0.skyscrapers.api.structure.TurnType.PlacementTurn
import net.lab0.skyscrapers.engine.editor
import net.lab0.skyscrapers.engine.exception.GameRuleViolationException

class RuleBook(
  val turnRules: List<Rule<TurnType>>,
  val placementRules: List<Rule<PlacementTurn>>,
  val moveOnlyRules: List<Rule<MoveOnly>>,
  val moveRules: List<Rule<Move>>,
  val buildRules: List<Rule<BuildTurn>>,
  val sealRules: List<Rule<SealTurn>>,
  val winRules: List<Rule<WinTurn>>,
) {
  fun tryToPlay(turn: TurnType, state: GameState): List<GameRuleViolation> {
    try {
      throwIfViolatedRule(turnRules, turn, state)

      when (turn) {
        is PlacementTurn ->
          throwIfViolatedRule(placementRules, turn, state)

        is TurnType.GiveUpTurn -> {} // check nothing

        is TurnType.MoveTurn -> {
          throwIfViolatedRule(moveOnlyRules, turn, state)
          throwIfViolatedRule(moveRules, turn, state)

          val nextBuilderState = state.editor().move(turn)

          when (turn) {
            is BuildTurn ->
              throwIfViolatedRule(buildRules, turn, nextBuilderState)

            is SealTurn ->
              throwIfViolatedRule(sealRules, turn, nextBuilderState)

            is WinTurn ->
              throwIfViolatedRule(winRules, turn, nextBuilderState)

          }
        }
      }
    } catch (violation: GameRuleViolationException) {
      return violation.violations
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

  fun canBuild(
    turn: BuildTurn,
    state: GameState,
  ): Boolean =
    canMove(turn, state) &&
        checkRules(buildRules, turn, state.editor().move(turn)).isEmpty()
}
