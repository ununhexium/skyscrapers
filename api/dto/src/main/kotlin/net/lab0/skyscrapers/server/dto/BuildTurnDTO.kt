package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable

@Serializable
data class BuildTurnDTO(
  val player: Int,
  val start: PositionDTO,
  val target: PositionDTO,
  val build: PositionDTO,
)