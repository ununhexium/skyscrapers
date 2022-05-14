package net.lab0.skyscrapers.logic.api

interface GameRuleViolation {
  val name: String
  val description: String
  val detail: String
}
