package net.lab0.skyscrapers.logic.structure

/**
 * @param width exclusive
 * @param height exclusive
 */
data class Bounds(val width: Int, val height: Int) {
  /**
   * @return each position, assuming minX = minY = 0 and
   * (width and height) are exclusive.
   */
  val positionsSequence
    get() =
      sequence {
        var index = 0

        while (index < width * height) {
          yield(Position(index % width, index / height))
          index++
        }
      }

  operator fun contains(pos: Position): Boolean =
    pos.inBounds(this)
}