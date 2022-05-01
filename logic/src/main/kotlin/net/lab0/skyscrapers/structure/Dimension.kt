package net.lab0.skyscrapers.structure

data class Dimension(val width: Int, val height: Int) {
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
}
