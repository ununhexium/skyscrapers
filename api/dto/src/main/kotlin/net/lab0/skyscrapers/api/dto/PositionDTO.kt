package net.lab0.skyscrapers.api.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.api.structure.Position

@Serializable
data class PositionDTO(val x: Int, val y: Int) {
  constructor(pos: Position) : this(pos.x, pos.y)

  fun toModel() =
    Position(x, y)
}