package net.lab0.skyscrapers.ai

import mu.KotlinLogging
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Phase
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.RuleBook

abstract class AbstractAi : Ai {
  val log = KotlinLogging.logger(this::class.qualifiedName!!)

  override fun think(
    player: Int,
    state: GameState,
    ruleBook: RuleBook
  ): TurnType {
    return when (state.phase) {
      Phase.PLACEMENT -> findPlacementTurn(player, state)
      Phase.MOVEMENT -> findMovementTurn(player, state, ruleBook)
      Phase.FINISHED -> throw IllegalStateException("Should not happen as the game will stop before calling the AI with such a state")
    } ?: TurnType.GiveUpTurn(player).also {
      log.warn { "The AI $name didn't know what to play. Giving up." }
    }
  }

  abstract fun findPlacementTurn(
    player: Int,
    state: GameState
  ): TurnType.PlacementTurn?

  abstract fun findMovementTurn(
    player: Int,
    state: GameState,
    ruleBook: RuleBook
  ): TurnType?
}
