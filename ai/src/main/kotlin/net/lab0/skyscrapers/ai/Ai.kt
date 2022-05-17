package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.engine.api.Game
import net.lab0.skyscrapers.engine.api.TurnType

interface Ai {
  val name: String

  fun think(game: Game): TurnType
}
