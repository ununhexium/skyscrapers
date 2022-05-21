package net.lab0.skyscrapers.engine.structure

import kotlin.math.abs

data class Position(
  val x: Int,
  val y: Int,
) {
  companion object {
    val NONE = Position(-1, -1)
  }

  /**
   * @return The distance between this position and the `other` position.
   */
  fun nextTo(other: Position): Boolean {
    if (this == other) return false // same position not accepted
    return abs(this.x - other.x) <= 1 && abs(this.y - other.y) <= 1
  }

  override fun toString() =
    "[$x, $y]"

  /**
   * Get the list of all surrounding positions, including diagonals
   */
  fun getSurroundingPositions() = listOf(
    Position(this.x + 1, this.y - 1),
    Position(this.x + 1, this.y),
    Position(this.x + 1, this.y + 1),

    Position(this.x, this.y - 1),
    Position(this.x, this.y + 1),

    Position(this.x - 1, this.y - 1),
    Position(this.x - 1, this.y),
    Position(this.x - 1, this.y + 1),
  )

  fun getSurroundingPositionsWithin(bounds: Bounds) = listOf(
    Position(this.x + 1, this.y - 1),
    Position(this.x + 1, this.y),
    Position(this.x + 1, this.y + 1),

    Position(this.x, this.y - 1),
    Position(this.x, this.y + 1),

    Position(this.x - 1, this.y - 1),
    Position(this.x - 1, this.y),
    Position(this.x - 1, this.y + 1),
  ).filter { it in bounds }
}
