package net.lab0.skyscrapers.api.structure

interface GameRuleViolation {
  val name: String
  val description: String
  val detail: String
}
