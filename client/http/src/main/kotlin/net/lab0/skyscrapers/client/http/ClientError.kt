package net.lab0.skyscrapers.client.http

import net.lab0.skyscrapers.api.structure.GameRuleViolation

sealed class ClientError {
  data class SimpleErrors(val errors: List<String>) : ClientError()
  data class GameRuleErrors(val violations: List<GameRuleViolation>) : ClientError()
}
