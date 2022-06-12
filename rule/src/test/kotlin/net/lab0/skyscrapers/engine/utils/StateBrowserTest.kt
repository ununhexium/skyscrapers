package net.lab0.skyscrapers.engine.utils

import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Position
import org.junit.jupiter.api.Test

/**
 * This tests assumes the usage of the default rules
 */
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
        |Players: A:a, B:a
      """.trimMargin()
    )

    val browser =
      net.lab0.skyscrapers.engine.utils.StateBrowser(
        state,
        Defaults.RULE_BOOK
      )

    assertThat(browser.getMovableBuilders(0)).isEmpty()
    assertThat(browser.getMovableBuilders(1)).isEqualTo(
      listOf(
        Position(2, 0),
        Position(2, 4)
      )
    )
  }

  @Test
  fun `a builder can move to an empty cell`() {
    val state = GameState.from(
      """
        |Board
        |  2  B0  0 0
        | (0) A0  0 0
        |Blocks: 0:0
        |Players: A:a, B:a
      """.trimMargin()
    )

    val browser =
      net.lab0.skyscrapers.engine.utils.StateBrowser(
        state,
        Defaults.RULE_BOOK
      )

    val builder = Position(1, 1)

    browser.builderCanMoveTo(builder, Position(0, 0)) shouldBe false
    browser.builderCanMoveTo(builder, Position(0, 1)) shouldBe false
    browser.builderCanMoveTo(builder, Position(1, 0)) shouldBe false
    browser.builderCanMoveTo(builder, Position(3, 0)) shouldBe false
    browser.builderCanMoveTo(builder, Position(1, 2)) shouldBe false

    // ok: empty
    browser.builderCanMoveTo(builder, Position(2, 0)) shouldBe true
  }
}