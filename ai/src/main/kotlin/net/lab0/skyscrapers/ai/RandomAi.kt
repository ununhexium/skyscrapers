package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Phase
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.rule.RuleBook
import net.lab0.skyscrapers.engine.utils.StateBrowser

class RandomAi(
  val player: Int,
  override val name: String = "RandomAi#$player"
) : Ai {

  data class RandomAction(
    val start: Position,
    val target: Position,
    val buildOrSeal: Position,
    val type: Type,
  ) {
    enum class Type {
      BUILD,
      SEAL
    }
  }

  override fun think(state: GameState, ruleBook: RuleBook): TurnType {
    return when (state.phase) {
      Phase.PLACEMENT -> findPlacementTurn(state)
      Phase.MOVEMENT -> findMovementTurn(state, ruleBook)
      Phase.FINISHED -> throw IllegalStateException("Should not happen as the game will stop before calling the AI with such a state")
    }
  }

  private fun findPlacementTurn(state: GameState): TurnType {
    val state = state

    val randomPosition = state
      .bounds
      .positionsSequence
      .filterNot { state.hasBuilder(it) }
      .shuffled()
      .first()

    return TurnType.PlacementTurn(player, randomPosition)
  }

  private fun findMovementTurn(state: GameState, ruleBook: RuleBook): TurnType {
    val browser = StateBrowser(state, ruleBook)

    val choices = browser
      .getTargetPositions(player)

      .flatMap { (start, target) ->
        target
          .getSurroundingPositionsWithin(state.bounds)
          .filter { state.builders[it] == null }
          .filterNot { state.seals[it] }
          .flatMap { buildOrSeal ->
            listOf(
              RandomAction(
                start,
                target,
                buildOrSeal,
                RandomAction.Type.BUILD
              ),
              RandomAction(
                start,
                target,
                buildOrSeal,
                RandomAction.Type.SEAL
              ),
            )
          }
      }

    val validatedTurns = choices.shuffled().map {
      when (it.type) {
        RandomAction.Type.BUILD -> TurnType.MoveTurn.BuildTurn(
          player,
          it.start,
          it.target,
          it.buildOrSeal
        )
        RandomAction.Type.SEAL -> TurnType.MoveTurn.SealTurn(
          player,
          it.start,
          it.target,
          it.buildOrSeal
        )
      }
    }
//      .filter {
//      game.ruleBook.tryToPlay(it, state).isEmpty()
//    }

    return validatedTurns.firstOrNull() ?: TurnType.GiveUpTurn(player)
  }
}
