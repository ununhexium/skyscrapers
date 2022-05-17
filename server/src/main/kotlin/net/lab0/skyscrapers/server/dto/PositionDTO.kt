package net.lab0.skyscrapers.server.dto

import kotlinx.serialization.Serializable
import net.lab0.skyscrapers.engine.structure.Position

@Serializable
data class PositionDTO(val x: Int, val y: Int) {
  constructor(pos: Position) : this(pos.x, pos.y)

  fun toPosition() =
    Position(x, y)
}
