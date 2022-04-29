package net.lab0.skyscrapers

import net.lab0.skyscrapers.state.GameStateData
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GameStateDataTest {
  companion object {
    val state = GameStateData(
      listOf(
        listOf(0, 0, 3, 4),
        listOf(0, 2, 0, 0),
        listOf(1, 0, 3, 0),
      ),
      listOf(
        listOf(true, false, false, true),
        listOf(false, true, false, false),
        listOf(false, false, true, false),
      ),
      listOf(
        listOf(0,1,null,0),
        listOf(1, null, null, 1),
        listOf(0, null, null, null),
      )
    )

  }

  @Test
  fun `can store the initial state from strings`() {
    val state = GameStateData.from(
      """
        1 0 0
        0 2 0
        3 0 3
        0 0 4
      """.trimIndent(),

      """
        1 0 0
        0 1 0
        0 0 1
        1 0 0
      """.trimIndent(),

      """
        0 1 0
        1 . .
        . . .
        0 1 .
      """.trimIndent(),
    )

    assertThat(state).isEqualTo(state)
  }

  @Test
  fun `can print the state`() {
    assertThat(state.toString()).isEqualTo(
      """
        |Buildings
        |1 0 0
        |0 2 0
        |3 0 3
        |0 0 4
        |
        |Seals
        |1 0 0
        |0 1 0
        |0 0 1
        |1 0 0
        |
        |Builders
        |0 1 0
        |1 . .
        |. . .
        |0 1 .
      """.trimMargin()
    )
  }
}