package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.dto.value.GameName

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