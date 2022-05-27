package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.api.structure.TurnType

interface Ai {
  val name: String

  fun think(game: Game): TurnType
}
