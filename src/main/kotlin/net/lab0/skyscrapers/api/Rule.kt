package net.lab0.skyscrapers.api

import net.lab0.skyscrapers.structure.GameState

interface Rule<T : Turn> {
  val name: String
  val description: String

  fun checkRule(state: GameState, turn: T): List<GameRuleViolation>
}
