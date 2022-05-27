package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.dto.value.GameName

@Serializable
data class GameResponse(
  val name: String,
  val state: StateDTO,
) {
  constructor(name: GameName, state: GameState) : this(
    name.value,
    StateDTO(
      bounds = BoundsDTO(state.bounds),
      players = state.players.map { PlayerDTO(it) },
      currentPlayer = state.currentPlayer,
      maxBuildersPerPlayer = state.maxBuildersPerPlayer,
      blocks = BlocksDataDTO(state.blocks),
      buildings = MatrixDTO<Int>(state.buildings.map { it.value }),
      seals = MatrixDTO<Boolean>(state.seals),
      builders = MatrixDTO<Int?>(state.builders),
      phase = PhaseDTO.from(state.phase),
    )
  )
}