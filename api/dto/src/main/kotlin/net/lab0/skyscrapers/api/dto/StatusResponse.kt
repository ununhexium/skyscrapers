package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.dto.value.GameName

@Serializable
data class StatusResponse(
  val status: String,
  val games: Set<String>,
) {
  companion object {
    fun of(status: String, games: Set<GameName>) =
      StatusResponse(status, games.map { it.value }.toSet())
  }
}