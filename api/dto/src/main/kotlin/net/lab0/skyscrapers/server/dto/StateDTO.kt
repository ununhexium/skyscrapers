package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable

@Serializable
data class StateDTO(
  val bounds: BoundsDTO,
  val players: List<PlayerDTO>,
  val currentPlayer: Int,
  val maxBuildersPerPlayer: Int,
  val blocks: BlocksDataDTO,
  val buildings: MatrixDTO<Int>,
  val seals: MatrixDTO<Boolean>,
  val builders: MatrixDTO<Int?>,
  val phase: PhaseDTO,
)