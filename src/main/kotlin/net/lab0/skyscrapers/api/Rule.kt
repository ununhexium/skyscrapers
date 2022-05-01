package net.lab0.skyscrapers.api

interface Rule<T : Turn> {
  val name: String
  val description: String

  fun checkRule(state: GameState, turn: T): List<GameRuleViolation>
}
