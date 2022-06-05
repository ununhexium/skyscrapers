package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.dto.value.GameName
import net.lab0.skyscrapers.api.structure.GameState

@Serializable
data class GameResponse(
  val name: String,
  val state: GameStateDTO,
) {
  constructor(name: GameName, state: GameState) : this(
    name.value,
    GameStateDTO(state)
  )
}