package net.lab0.skyscrapers.api.structure

data class Bounds(val abscissaRange: IntRange, val ordinateRange: IntRange) {

  companion object {
    val ZERO = Bounds(0..0, 0..0)
    val EMPTY = Bounds(IntRange.EMPTY, IntRange.EMPTY)
  }

  /**
   * @return each position, assuming minX = minY = 0 and
   * (width and height) are exclusive.
   */
  val positionsSequence
    get() =
      sequence {
        abscissaRange.forEach { x ->
          ordinateRange.forEach { y ->
            yield(Position(x, y))
          }
        }
      }

  operator fun contains(pos: Position): Boolean =
    pos.x in abscissaRange && pos.y in ordinateRange
}
