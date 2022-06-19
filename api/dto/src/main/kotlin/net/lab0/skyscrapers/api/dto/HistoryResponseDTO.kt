package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.GameState

@Serializable
data class HistoryResponseDTO(val history: List<GameStateDTO>) {
  fun toModel(): List<GameState> =
    history.map { it.toModel() }

  companion object {
    fun from(history: List<GameState>) =
      HistoryResponseDTO(history.map { GameStateDTO(it) })
  }
}
