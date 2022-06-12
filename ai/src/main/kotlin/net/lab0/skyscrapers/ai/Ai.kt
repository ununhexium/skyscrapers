package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType

interface Ai {
  val name: String

  fun think(game: GameState): TurnType
}
