package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.engine.api.TurnType
import net.lab0.skyscrapers.engine.structure.Phase
import net.lab0.skyscrapers.engine.structure.Position

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

  override fun think(game: Game): TurnType {
    return when (game.state.phase) {
      Phase.PLACEMENT -> findPlacementTurn(game)
      Phase.MOVEMENT -> findMovementTurn(game)
      Phase.FINISHED -> throw IllegalStateException("Should not happen as the game will stop before calling the AI with such a state")
    }
  }

  private fun findPlacementTurn(game: Game): TurnType {
    val state = game.state

    val randomPosition = state
      .bounds
      .positionsSequence
      .filterNot { state.hasBuilder(it) }
      .shuffled()
      .first()

    return TurnType.PlacementTurn(player, randomPosition)
  }

  private fun findMovementTurn(game: Game): TurnType {
    val state = game.state

    val choices = state
      .getBuilders(player)
      .flatMap { start ->
        start
          .getSurroundingPositions()
          .filter { it in state.bounds }
          .filterNot { state.hasBuilder(it) }
          .flatMap { target ->
            target
              .getSurroundingPositions()
              .filter { it in state.bounds }
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
    }.filter {
      game.ruleBook.tryToPlay(it, state).isEmpty()
    }

    return validatedTurns.firstOrNull() ?: TurnType.GiveUpTurn(player)
  }
}
