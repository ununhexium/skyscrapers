package net.lab0.skyscrapers.api

interface Rule<T : Turn> {
  val name: String
  val description: String

  fun checkRule(game: GameState, turn: T): List<GameRuleViolation>
}
