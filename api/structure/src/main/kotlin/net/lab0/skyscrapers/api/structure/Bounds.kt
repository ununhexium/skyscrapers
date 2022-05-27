package net.lab0.skyscrapers.api.structure

/**
 * @param width exclusive
 * @param height exclusive
 */
// TODO: bounds should be defined by ranges, not width / height
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
    pos.x in 0 until width && pos.y in 0 until height
}
