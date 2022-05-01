package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.Game
import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.api.TurnType
import net.lab0.skyscrapers.structure.Phase

class Arbiter(val ais: List<Ai>) {
  constructor(vararg ais: Ai) : this(ais.toList())

  fun startMatch(
    stateObserver: ((GameState) -> Unit)?,
    turnObserver: ((TurnType) -> Unit)?,
    phaseObserver: ((Phase) -> Unit)?,
  ) {
    val g = Game.new(playerCount = ais.size)
    phaseObserver?.let { it(g.state.phase) }

    stateObserver?.let { it(g.state) }

    while (g.state.phase != Phase.FINISHED) {
      val ai = ais[g.state.currentPlayer]
      val turn = ai.think(g.state)

      turnObserver?.let { it(turn) }
      g.play(turn)
      stateObserver?.let { it(g.state) }

      val (previousState, currentState) = g.history.takeLast(2)
      if (previousState.phase != currentState.phase) {
        phaseObserver?.let { it(currentState.phase) }
      }
    }
  }
}
