package net.lab0.skyscrapers.client.shell.spring.data

import net.lab0.skyscrapers.api.structure.Errors
import net.lab0.skyscrapers.api.structure.GameState

sealed class HierarchyResult {
  data class StateUpdate(val state: GameState) : HierarchyResult()
  data class Error(val errors: Errors) : HierarchyResult()
}
