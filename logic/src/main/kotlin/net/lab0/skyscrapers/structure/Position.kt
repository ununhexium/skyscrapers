package net.lab0.skyscrapers.structure

import kotlin.math.abs

data class Position(
  val x: Int,
  val y: Int,
) {
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
   * @param maxX inclusive
   * @param maxY inclusive
   */
  fun inBounds(minX: Int, maxX: Int, minY: Int, maxY: Int) =
    x in minX..maxX && y in minY..maxY

  fun inBounds(dimensions:Dimension) =
    x in 0 until dimensions.width && y in 0 until dimensions.height

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
}
