package net.lab0.skyscrapers.server

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.logic.api.GameState
import net.lab0.skyscrapers.server.dto.SerializableBlocksData
import net.lab0.skyscrapers.server.dto.SerializableBounds
import net.lab0.skyscrapers.server.dto.SerializableMatrix
import net.lab0.skyscrapers.server.dto.SerializablePlayer
import net.lab0.skyscrapers.server.dto.SerializedState

@Serializable
data class GameResponse(
  val name: String,
  val state: SerializedState,
) {
  constructor(name:String, state: GameState) : this(
    name,
    SerializedState(
      SerializableBounds(state.bounds),
      state.players.map { SerializablePlayer(it) },
      state.maxBuildersPerPlayer,
      SerializableBlocksData(state.blocks),
      SerializableMatrix<Int>(state.buildings.map { it.value }),
      SerializableMatrix<Boolean>(state.seals),
      SerializableMatrix<Int?>(state.builders),
    )
  )
}

