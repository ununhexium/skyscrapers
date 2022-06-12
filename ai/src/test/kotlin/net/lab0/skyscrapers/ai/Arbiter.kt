package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Phase
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.engine.GameFactoryImpl

class Arbiter(val ais: List<Ai>) {
  constructor(vararg ais: Ai) : this(ais.toList())

  fun playMatch(
    stateObserver: ((GameState) -> Unit)?,
    turnObserver: ((TurnType) -> Unit)?,
    phaseObserver: ((Phase) -> Unit)?,
  ) {
    val game = GameFactoryImpl().new(playerCount = ais.size)
    phaseObserver?.let { it(game.state.phase) }

    stateObserver?.let { it(game.state) }

    while (game.state.phase != Phase.FINISHED) {
      val ai = ais[game.state.currentPlayer]
      val turn = ai.think(game.state)

      turnObserver?.let { it(turn) }
      game.play(turn)
      stateObserver?.let { it(game.state) }

      val (previousState, currentState) = game.history.takeLast(2)
      if (previousState.phase != currentState.phase) {
        phaseObserver?.let { it(currentState.phase) }
      }
    }
  }
}