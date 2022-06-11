package net.lab0.skyscrapers.client.shell.spring.data

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.client.http.ClientError
import net.lab0.skyscrapers.client.http.ClientError.GameRuleErrors
import net.lab0.skyscrapers.client.http.ClientError.SimpleErrors

sealed class HierarchyResult {

  data class StateUpdate(val state: GameState, val comment: String) :
    HierarchyResult()

  data class Error(val error: ClientError) : HierarchyResult() {
    // TODO: extract to result handler component
    override fun toString(): String =
      "Error when playing the game:\n" + when (error) {
        // TODO: the output should be a sealed class that separates the normal string and the error responses
        is GameRuleErrors -> error.violations.joinToString(
          separator = "\n"
        )
        is SimpleErrors -> error.errors.joinToString(
          separator = "\n"
        )
      }
  }
}
