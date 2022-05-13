package net.lab0.skyscrapers.utils

import net.lab0.skyscrapers.Defaults
import net.lab0.skyscrapers.api.GameState
import net.lab0.skyscrapers.structure.Position
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StateBrowserTest {
  @Test
  fun `can get movable builders`() {
    val state = GameState.from(
      """
        |Board
        | A0  (0) B2  3 A0
        | (0) (0)  9  2  4
        |  0   0   0  0  0
        |  0   0   0  0  0
        |  0   0  B3  0  0
        |Blocks: 0:0
        |Players: 0:a, 1:a
      """.trimMargin()
    )

    val browser = StateBrowser(state, Defaults.RULE_BOOK)

    assertThat(browser.getMovableBuilders(0)).isEmpty()
    assertThat(browser.getMovableBuilders(1)).isEqualTo(
      listOf(
        Position(2, 0),
        Position(2, 4)
      )
    )
  }

}