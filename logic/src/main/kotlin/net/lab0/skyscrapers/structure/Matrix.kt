package net.lab0.skyscrapers.structure


/**
 * A MxN matrix (rows x column)
 */
data class Matrix<T>(
  val data: List<List<T>>,
  val rows: Int = data.size,
  val columns: Int = data.first().size,
) {
  val dimensions = Bounds(columns, rows)

  val lastColumn = columns - 1
  val lastRow = rows - 1

  constructor(rows: Int, columns: Int, generator: (Position) -> T) : this(
    (0 until rows).map { r ->
      (0 until columns).map { c ->
        generator(Position(c, r))
      }
    }
  )

  init {
    // check that the properties are correct
    if (data.size != rows)
      throw IllegalArgumentException("The provided data size (${data.size}) doesn't match the expected N dimension ($columns)")

    val problem = data.firstOrNull { it.size != columns }
    if (problem != null)
      throw IllegalArgumentException("The provided data size (${problem.size}) doesn't match the expected M dimension ($rows)")
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

  /**
   * @return The same matrix but with the given value set at (row, column)
   */
  fun copyAndSet(row: Int, column: Int, value: T) =
    Matrix(
      data.mapIndexed { r, internalRow ->
        internalRow.mapIndexed { c, cell ->
          if (c == column && r == row) value else cell
        }
      }
    )

  fun copyAndSwap(pos0: Position, pos1: Position) =
    copyAndSwap(pos0.y, pos0.x, pos1.y, pos1.x)

  fun copyAndSwap(row0: Int, column0: Int, row1: Int, column1: Int) =
    Matrix(
      data.map { it.toMutableList() }.toMutableList().also {
        val tmp = it[row0][column0]
        it[row0][column0] = it[row1][column1]
        it[row1][column1] = tmp
      }
    )

  fun copyAndSet(pos: Position, value: T) =
    copyAndSet(pos.y, pos.x, value)

  fun <R> map(transform: (T) -> R) =
    Matrix(data.map { row -> row.map { transform(it) } })

  fun <R> mapIndexed(transform: (Position, T) -> R) =
    data.mapIndexed { r, row ->
      row.mapIndexed { c, cell ->
        transform(Position(c, r), cell)
      }
    }

  fun <R> mapIndexedTo(
    collection: MutableCollection<R>,
    transform: (Position, T) -> R
  ) {
    collection.addAll(
      data.flatMapIndexed { r, row ->
        row.mapIndexed { c, cell ->
          transform(Position(c, r), cell)
        }
      }
    )
  }

  fun count(function: (T) -> Boolean) =
    data.sumOf { row ->
      row.count { cell ->
        function(cell)
      }
    }

  fun <A, Z> merge(
    other: Matrix<A>,
    combiner: (Pair<T, A>) -> Z
  ): Matrix<Z> {
    if (this.dimensions != other.dimensions)
      throw IllegalArgumentException("The 2 matrices must have the same size. You gave $dimensions and ${other.dimensions}")

    return Matrix(
      data
        .flatten()
        .zip(other.data.flatten())
        .map { combiner(it) }
        .chunked(columns)
    )
  }

  // TODO: add contract to assert that the reult type T is not null?
  fun filter(predicate: (T) -> Boolean) =
    mutableMapOf<Position, T>().also { m ->
      this@Matrix.forEachIndexed { pos, it -> if (predicate(it)) m[pos] = it }
    }

  private fun forEachIndexed(f: (Position, T) -> Unit) =
    data.forEachIndexed { r, row ->
      row.forEachIndexed { c, cell ->
        f(Position(c, r), cell)
      }
    }

}
