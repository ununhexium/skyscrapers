package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.Position

@Serializable
data class SealTurnDTO(
  val start: PositionDTO,
  val target: PositionDTO,
  val seal: PositionDTO,
) {
  constructor(
    start: Position,
    target: Position,
    seal: Position,
  ) : this(
    PositionDTO(start),
    PositionDTO(target),
    PositionDTO(seal),
  )
}
