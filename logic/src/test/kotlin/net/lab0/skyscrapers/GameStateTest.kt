package net.lab0.skyscrapers

import net.lab0.skyscrapers.api.BlocksData
import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.structure.Dimension
import net.lab0.skyscrapers.structure.Height
import net.lab0.skyscrapers.structure.Matrix
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class GameStateTest {
  companion object {
    val state = GameState(
      Dimension(4,3),
      listOf(),
      0,
      BlocksData.EMPTY,
      Matrix(
        listOf(
          listOf(1, 0, 3, 4),
          listOf(0, 2, 0, 0),
          listOf(0, 0, 3, 0),
        )
      ).map { Height(it) },
      Matrix(
        listOf(
          listOf(true, false, false, true),
          listOf(false, true, false, false),
          listOf(false, false, true, false),
        )
      ),
      Matrix(
        listOf(
          listOf(0, 1, null, 0),
          listOf(1, null, null, 1),
          listOf(0, null, null, null),
        )
      )
    )
  }

  @Test
  fun `can store the initial state from strings`() {
    val parsed = GameState.from(
      listOf(),
      0,
      BlocksData.EMPTY,
      """
          1 0 3 4
          0 2 0 0
          0 0 3 0
      """.trimIndent(),

      """
        1 0 0 1
        0 1 0 0
        0 0 1 0
      """.trimIndent(),

      """
        0 1 . 0
        1 . . 1
        0 . . .
      """.trimIndent(),
    )

    assertThat(parsed).isEqualTo(state)
  }

  @Test
  fun `can print the state`() {
    assertThat(state.toString()).isEqualTo(
      """
        |Buildings
        |1 0 3 4
        |0 2 0 0
        |0 0 3 0
        |
        |Seals
        |1 0 0 1
        |0 1 0 0
        |0 0 1 0
        |
        |Builders
        |0 1 . 0
        |1 . . 1
        |0 . . .
      """.trimMargin()
    )
  }

  @Test
  fun `can't have matrices of different sizes`() {

    // seals matrix has a different size
    assertThrows<IllegalStateException> {
      GameState(
        Dimension(0,0),
        listOf(),
        0,
        BlocksData.EMPTY,
        Matrix(3, 4) { Height(0) },
        Matrix(5, 5) { false },
        Matrix(3, 4) { 0 },
      )
    }

    // builders matrix has a different size
    assertThrows<IllegalStateException> {
      GameState(
        Dimension(0,0),
        listOf(),
        0,
        BlocksData.EMPTY,
        Matrix(3, 4) { Height(0) },
        Matrix(3, 4) { false },
        Matrix(5, 5) { 0 },
      )
    }

  }
}