package net.lab0.skyscrapers.ai

import net.lab0.skyscrapers.api.dto.RuleBook
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType

interface Ai {
  val name: String

  fun think(state: GameState, ruleBook: RuleBook): TurnType
}
