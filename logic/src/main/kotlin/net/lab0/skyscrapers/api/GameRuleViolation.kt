package net.lab0.skyscrapers.api

interface GameRuleViolation {
  val name: String
  val description: String
  val detail: String
}
