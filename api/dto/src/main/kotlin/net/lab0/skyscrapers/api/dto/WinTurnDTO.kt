package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.Position

@Serializable
data class WinTurnDTO(
  val start: PositionDTO,
  val target: PositionDTO,
) {
  constructor(start: Position, target: Position) :
      this(PositionDTO(start), PositionDTO(target))
}
