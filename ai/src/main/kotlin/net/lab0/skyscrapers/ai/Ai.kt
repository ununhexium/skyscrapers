package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.logic.api.Game
import net.lab0.skyscrapers.logic.api.TurnType

interface Ai {
  val name: String

  fun think(game: Game): TurnType
}
