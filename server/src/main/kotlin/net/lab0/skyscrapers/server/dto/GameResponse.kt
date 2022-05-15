package net.lab0.skyscrapers.server

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.server.dto.BlocksDataDTO
import net.lab0.skyscrapers.server.dto.BoundsDTO
import net.lab0.skyscrapers.server.dto.MatrixDTO
import net.lab0.skyscrapers.server.dto.PhaseDTO
import net.lab0.skyscrapers.server.dto.PlayerDTO
import net.lab0.skyscrapers.server.dto.StateDTO
import net.lab0.skyscrapers.server.value.GameName

@Serializable
data class GameResponse(
  val name: String,
  val state: StateDTO,
) {
  constructor(name:GameName, state: GameState) : this(
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

