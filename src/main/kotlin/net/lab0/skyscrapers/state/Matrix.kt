package net.lab0.skyscrapers.state

import net.lab0.skyscrapers.Position


/**
 * A MxN matrix (rows x column)
 */
data class Matrix<T>(
  val data: List<List<T>>,
  val M: Int = data.size,
  val N: Int = data.first().size,
) {
  init {
    // check that the properties are correct
    if (data.size != M)
      throw IllegalArgumentException("The provided data size (${data.size}) doesn't match the expected N dimension ($N)")

    val problem = data.firstOrNull { it.size != N }
    if (problem != null)
      throw IllegalArgumentException("The provided data size (${problem.size}) doesn't match the expected M dimension ($M)")
  }

  companion object {
    fun <T> from(
      input: String,
      rowSeparator: String = "\n",
      columnSeparator: Regex = Regex(" +"),
      converter: (String) -> T,
    ) = Matrix(
      input.split(rowSeparator).map { row ->
        row.split(columnSeparator).map { converter(it) }
      }
    )
  }

  operator fun get(row: Int, column: Int) =
    data[row][column]

  operator fun get(pos: Position) =
    get(pos.y, pos.x)

  /**
   * Prints the matrix as follows
   *
   * ```
   *   X   0   1   2   3   .   .   .   N
   * Y
   * 0     a00 a01 a02 a03 .   .   .   a1N
   * 1     a10 a11 a12 a13 .   .   .   a1N
   * 2     a20 a21 a22 a23 .   .   .   a2N
   * .
   * .
   * .
   * M     a10 a11 a12 a13 .   .   .   a1N
   *
   * ```
   */
  fun toString(converter: (T) -> String) =
    data.joinToString("\n") { row ->
      row.joinToString(" ", transform = converter)
    }

  override fun toString() =
    toString { element -> element.toString() }
}
