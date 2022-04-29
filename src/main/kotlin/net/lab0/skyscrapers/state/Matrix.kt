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
      columnSeparator: String = " ",
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
   *   X 0 1 2 3 . . . M
   * Y
   * 0
   * 1
   * 2
   * .
   * .
   * .N
   *
   * ```
   */
  override fun toString() =
    data.joinToString("\n") { row ->
      row.joinToString(" ") { it.toString() }
    }
}
