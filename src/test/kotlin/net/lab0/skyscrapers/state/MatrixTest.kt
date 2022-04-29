package net.lab0.skyscrapers.state

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
    assertThat(matrix[0,0]).isEqualTo(1)
    assertThat(matrix[1,2]).isEqualTo(6)
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
    Matrix.from(
      """
        |1 2 3
        |4 5 6
      """.trimMargin()
    ) { it.toInt() }
  }
}