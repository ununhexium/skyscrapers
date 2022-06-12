package net.lab0.skyscrapers.engine.utils

import io.kotest.matchers.shouldBe
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.Position
import net.lab0.skyscrapers.engine.Defaults
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * This tests assumes the usage of the default rules
 */
internal class StateBrowserTest {
  @Test
  fun `can get builders`() {
    val state = GameState.from(
      """
        |Board
        | A0   0  B0  0 A0
        |  0   0   0  0  0
        |Blocks: 0:0
        |Players: 0:a, 1:a
      """.trimMargin()
    )

    val browser = StateBrowser(state, Defaults.RULE_BOOK)

    browser.getBuilderPositionsForPlayer(0).toSet() shouldBe
        setOf(Position(0, 0), Position(4, 0))

    browser.getBuilderPositionsForPlayer(1).toSet() shouldBe
        setOf(Position(2, 0))

  }

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

    assertThat(browser.getMovableBuilders(0).toList()).isEmpty()
    assertThat(browser.getMovableBuilders(1).toList()).isEqualTo(
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
        |Players: 0:a, 1:a
      """.trimMargin()
    )

    val browser = StateBrowser(state, Defaults.RULE_BOOK)

    val builder = Position(1, 1)


    assertThat(browser.builderCanMoveTo(builder, Position(0, 0)))
      .describedAs("too high")
      .isFalse()

    assertThat(browser.builderCanMoveTo(builder, Position(0, 1)))
      .describedAs("sealed")
      .isFalse()

    assertThat(browser.builderCanMoveTo(builder, Position(1, 0)))
      .describedAs("occupied")
      .isFalse()

    assertThat(browser.builderCanMoveTo(builder, Position(3, 0)))
      .describedAs("far")
      .isFalse()

    assertThat(browser.builderCanMoveTo(builder, Position(1, 2)))
      .describedAs("outside")
      .isFalse()

    // ok: empty
    assertThat(browser.builderCanMoveTo(builder, Position(2, 0))).isTrue()

  }

  @Test
  fun `get winnable builders`() {
    val state = GameState.from(
      """
        |Board
        | A1  B1  2 0
        | A0   2 B0 0
        |Blocks: 0:0, 1:0, 2:0
        |Players: 0:a, 1:a
      """.trimMargin()
    )

    val browser = StateBrowser(state, Defaults.RULE_BOOK)

    browser.getWinnableBuilders(0).toSet() shouldBe
        setOf(Movement(Position(0, 0), Position(1, 1)))

    browser.getWinnableBuilders(1).toSet() shouldBe
        setOf(
          Movement(Position(1, 0), Position(2, 0)),
          Movement(Position(1, 0), Position(1, 1)),
        )
  }
}
