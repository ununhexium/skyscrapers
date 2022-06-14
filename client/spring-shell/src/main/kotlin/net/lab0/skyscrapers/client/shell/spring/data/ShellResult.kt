package net.lab0.skyscrapers.client.shell.spring.data

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.client.http.ClientError

sealed class ShellResult {
  sealed class Ok : ShellResult() {
    data class Text(val output: String) : Ok()
    data class StateUpdate(val state: GameState, val comment: String) : Ok()
  }

  sealed class Problem : ShellResult() {
    data class Text(val output: String) : Problem()
    data class Client(val error: ClientError) : Problem() {
      // TODO: extract to result handler component
      override fun toString(): String =
        "Error when playing the game:\n" + when (error) {
          // TODO: the output should be a sealed class that separates the normal string and the error responses
          is ClientError.GameRuleErrors -> error.violations.joinToString("\n")
          is ClientError.SimpleErrors -> error.errors.joinToString("\n")
        }
    }
  }
}
