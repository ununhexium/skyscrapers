package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable

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