package net.lab0.skyscrapers.server

import kotlinx.serialization.Serializable

@Serializable
data class GameResponse(val state: SerializedState)

@Serializable
data class SerializedState(
  val bounds: SerializableBounds,
  val players: List<SerializablePlayer>,
  val maxBuildersPerPlayer: Int,
  val blocks: SerializableBlocksData,
  val buildings: SerializableMatrix<Int>,
  val seals: SerializableMatrix<Boolean>,
  val builders: SerializableMatrix<Int?>,
)

@Serializable
data class SerializableBounds(
  val width: Int,
  val height: Int
)

@Serializable
data class SerializablePlayer(val id: Int, val active: Boolean)

@Serializable
data class SerializableBlocksData(val blocks: Map<Int, Int>)

@Serializable
data class SerializableMatrix<T>(val data: List<List<T>>)
