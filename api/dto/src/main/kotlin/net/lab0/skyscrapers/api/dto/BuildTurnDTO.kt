package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.Position

@Serializable
data class BuildTurnDTO(
  val start: PositionDTO,
  val target: PositionDTO,
  val build: PositionDTO,
) {
  constructor(
    start: Position,
    target: Position,
    build: Position,
  ) : this(
    PositionDTO(start),
    PositionDTO(target),
    PositionDTO(build),
  )
}
