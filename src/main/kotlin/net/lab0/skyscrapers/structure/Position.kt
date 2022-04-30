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
    if(this == other) return false // same position not accepted
    return abs(this.x - other.x) <= 1 && abs(this.y - other.y) <= 1
  }

  override fun toString() =
    "[$x, $y]"
}
