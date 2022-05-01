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
      Dimension(4, 3),
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
        Dimension(0, 0),
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
        Dimension(0, 0),
        listOf(),
        0,
        BlocksData.EMPTY,
        Matrix(3, 4) { Height(0) },
        Matrix(3, 4) { false },
        Matrix(5, 5) { 0 },
      )
    }
  }

  @Test
  fun `can show a composite board`() {
    val state = GameState(
      dimentions = Dimension(5, 5),
      players = listOf(Player(0, true), Player(1, false)),
      maxBuildersPerPlayer = 2,
      blocks = BlocksData(
        mapOf(
          Height(0) to 3,
          Height(1) to 13,
          Height(2) to 18,
          Height(3) to 13,
        )
      ),
      buildings = Matrix(
        listOf(
          listOf(0, 0, 1, 0, 0),
          listOf(3, 1, 1, 0, 0),
          listOf(0, 0, 0, 0, 1),
          listOf(0, 1, 0, 0, 1),
          listOf(0, 0, 0, 1, 0),
        )
      ).map { Height(it) },
      seals = Matrix(
        listOf(
          listOf(false, true, false, false, false),
          listOf(true, true, true, true, false),
          listOf(false, false, false, true, true),
          listOf(false, true, false, true, false),
          listOf(true, true, true, true, true),
        )
      ),
      builders = Matrix(
        listOf(
          listOf(null, null, null, null, null),
          listOf(null, null, null, null, null),
          listOf(null, 1, 0, null, null),
          listOf(0, null, null, null, 1),
          listOf(null, null, null, null, null),
        )
      )
    )

    assertThat(state.toCompositeString()).isEqualTo(
      """
        |Board
        |  0 (0)   1   0   0
        |(3) (1) (1) (0)   0
        |  0  B0  A0 (0) (1)
        | A0 (1)   0 (0)  B1
        |(0) (0) (0) (1) (0)
        |Blocks: 0:3, 1:13, 2:18, 3:13
        |Players: 0:a, 1:d
      """.trimMargin()
    )
  }

  @Test
  fun `can parse a composite board`() {
    val state = GameState(
      dimentions = Dimension(5, 5),
      players = listOf(Player(0, true), Player(1, false)),
      maxBuildersPerPlayer = 2,
      blocks = BlocksData(
        mapOf(
          Height(0) to 3,
          Height(1) to 13,
          Height(2) to 18,
          Height(3) to 13,
        )
      ),
      buildings = Matrix(
        listOf(
          listOf(0, 0, 1, 0, 0),
          listOf(3, 1, 1, 0, 0),
          listOf(0, 0, 0, 0, 1),
          listOf(0, 1, 0, 0, 1),
          listOf(0, 0, 0, 1, 0),
        )
      ).map { Height(it) },
      seals = Matrix(
        listOf(
          listOf(false, true, false, false, false),
          listOf(true, true, true, true, false),
          listOf(false, false, false, true, true),
          listOf(false, true, false, true, false),
          listOf(true, true, true, true, true),
        )
      ),
      builders = Matrix(
        listOf(
          listOf(null, null, null, null, null),
          listOf(null, null, null, null, null),
          listOf(null, 1, 0, null, null),
          listOf(0, null, null, null, 1),
          listOf(null, null, null, null, null),
        )
      )
    )


    assertThat(
      GameState.from(
        """
        |Board
        |  0 (0)   1   0   0
        |(3) (1) (1) (0)   0
        |  0  B0  A0 (0) (1)
        | A0 (1)   0 (0)  B1
        |(0) (0) (0) (1) (0)
        |Blocks: 0:3, 1:13, 2:18, 3:13
        |Players: 0:a, 1:d
      """.trimMargin()
      )
    ).isEqualTo(state)
  }
}