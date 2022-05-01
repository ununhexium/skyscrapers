package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.Game
import net.lab0.skyscrapers.api.TurnType

interface Ai {
  val name: String

  fun think(game: Game): TurnType
}
