package net.lab0.skyscrapers.engine.api

interface GameRuleViolation {
  val name: String
  val description: String
  val detail: String
}
