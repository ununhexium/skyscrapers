package net.lab0.skyscrapers.structure

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MatrixTest {
  companion object {
    val matrix = Matrix(
      listOf(
        listOf(1, 2, 3),
        listOf(4, 5, 6),
      )
    )
  }

  @Test
  fun `can get elements from the matrix`() {
    assertThat(matrix[0, 0]).isEqualTo(1)
    assertThat(matrix[0, 2]).isEqualTo(3)
    assertThat(matrix[1, 0]).isEqualTo(4)
    assertThat(matrix[1, 2]).isEqualTo(6)
  }

  @Test
  fun `can convert a matrix to a string`() {
    assertThat(matrix.toString()).isEqualTo(
      """
        |1 2 3
        |4 5 6
      """.trimMargin()
    )
  }

  @Test
  fun `can parse a string into a matrix`() {
    val m = Matrix.from(
      """
        |1 2 3
        |4 5 6
      """.trimMargin()
    ) { it.toInt() }

    assertThat(m).isEqualTo(matrix)
  }

  @Test
  fun `can parse matrix with nullable fields`() {
    val m = Matrix.from(
      """
        |1 . .
        |. . 6
      """.trimMargin()
    ) { if (it == ".") null else it.toInt() }

    assertThat(m).isEqualTo(
      Matrix(
        listOf(
          listOf(1, null, null),
          listOf(null, null, 6),
        )
      )
    )
  }

  @Test
  fun `can parse matrix with variable spacing`() {
    val m = Matrix.from(
      """
        |1  1
        |99 99
      """.trimMargin()
    ) { if (it == ".") null else it.toInt() }

    assertThat(m).isEqualTo(
      Matrix(
        listOf(
          listOf(1, 1),
          listOf(99, 99),
        )
      )
    )
  }

  @Test
  fun `can swap elements`() {
    val data = listOf(
      listOf(1, 2),
      listOf(4, 5),
    )

    val matrix = Matrix(data)
    val reference = Matrix(data)

    val onePosition = Position(0, 0)
    val fourPosition = Position(0, 1)

    val swapped = matrix.copyAndSwap(onePosition, fourPosition)

    // matrix is immutable
    assertThat(matrix).isEqualTo(reference)

    assertThat(swapped).isEqualTo(
      Matrix(
        listOf(
          listOf(4, 2),
          listOf(1, 5),
        )
      )
    )
  }

  @Test
  fun `can zip matrices`() {
    val one = Matrix(
      listOf(
        listOf(1, 2, 3),
        listOf(4, 5, 6),
      )
    )
    val a = Matrix(
      listOf(
        listOf("a", "b", "c"),
        listOf("d", "e", "f"),
      )
    )

    assertThat(one.merge(a){ it.first.toString() + it.second.toString() }).isEqualTo(
      Matrix(
        listOf(
          listOf("1a", "2b", "3c"),
          listOf("4d", "5e", "6f"),
        )
      )
    )
  }
}