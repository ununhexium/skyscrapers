package net.lab0.skyscrapers.engine.utils

import io.kotest.matchers.shouldBe
import net.lab0.skyscrapers.api.structure.GameState
import net.lab0.skyscrapers.api.structure.TurnType
import net.lab0.skyscrapers.api.structure.TurnType.MoveTurn.BuildTurn
import net.lab0.skyscrapers.engine.Defaults
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import net.lab0.skyscrapers.api.structure.Position as P

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
        setOf(P(0, 0), P(4, 0))

    browser.getBuilderPositionsForPlayer(1).toSet() shouldBe
        setOf(P(2, 0))

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
        P(2, 0),
        P(2, 4)
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

    val builder = P(1, 1)


    assertThat(browser.builderCanMoveTo(builder, P(0, 0)))
      .describedAs("too high")
      .isFalse()

    assertThat(browser.builderCanMoveTo(builder, P(0, 1)))
      .describedAs("sealed")
      .isFalse()

    assertThat(browser.builderCanMoveTo(builder, P(1, 0)))
      .describedAs("occupied")
      .isFalse()

    assertThat(browser.builderCanMoveTo(builder, P(3, 0)))
      .describedAs("far")
      .isFalse()

    assertThat(browser.builderCanMoveTo(builder, P(1, 2)))
      .describedAs("outside")
      .isFalse()

    // ok: empty
    assertThat(browser.builderCanMoveTo(builder, P(2, 0))).isTrue()

  }

  @Test
  fun `get winnable builders`() {
    val state = GameState.from(
      """
        |Board
        | A1  B1  2  0
        | A0   2 B0  0
        |  0  A1  0 C0
        |Blocks: 0:0, 1:0, 2:0
        |Players: A:a, B:a, C:a
      """.trimMargin()
    )

    val browser = StateBrowser(state, Defaults.RULE_BOOK)

    browser.getWinnableBuilders(0).toSet() shouldBe
        setOf(
          Movement(P(0, 0), P(1, 1)),
          Movement(P(1, 2), P(1, 1)),
        )

    browser.getWinnableBuilders(1).toSet() shouldBe
        setOf(
          Movement(P(1, 0), P(2, 0)),
          Movement(P(1, 0), P(1, 1)),
        )

    browser.getWinnableBuilders(2).toSet() shouldBe emptySet()
  }

  @Test
  fun `get target positions`() {
    val state = GameState.from(
      """
        |Board
        | A0  B1  2 0
        | A2   2 B0 0
        |Blocks: 0:0, 1:0, 2:0
        |Players: 0:a, 1:a
      """.trimMargin()
    )

    val browser = StateBrowser(state, Defaults.RULE_BOOK)

    browser.getTargetPositions(0).toSet() shouldBe
        setOf(
          Movement(P(0,1), P(1,1))
        )

    browser.getTargetPositions(1).toSet() shouldBe
        setOf(
          Movement(P(1,0), P(1,1)),
          Movement(P(1,0), P(2,0)),
          Movement(P(2,1), P(3,0)),
          Movement(P(2,1), P(3,1)),
        )
  }

  @Test
  fun `get buildable turns`() {
    val state = GameState.from(
      """
        |Board
        | A0  B1  2 0
        | A2   2 B0 0
        |Blocks: 0:10, 1:10, 2:10, 3:10
        |Players: 0:a, 1:a
      """.trimMargin()
    )

    val browser = StateBrowser(state, Defaults.RULE_BOOK)

    browser.getBuildableTurns(0).toSet() shouldBe
        setOf(
          BuildTurn(0, P(0,1), P(1,1), P(0,1)),
          BuildTurn(0, P(0,1), P(1,1), P(2,0)),
        )

    browser.getBuildableTurns(1).toSet() shouldBe
        setOf(
          BuildTurn(1, P(1,0), P(1,1), P(1,0)),
          BuildTurn(1, P(1,0), P(1,1), P(2,0)),

          BuildTurn(1, P(1,0), P(2,0), P(1,0)),
          BuildTurn(1, P(1,0), P(2,0), P(1,1)),
          BuildTurn(1, P(1,0), P(2,0), P(3,0)),
          BuildTurn(1, P(1,0), P(2,0), P(3,1)),

          BuildTurn(1, P(2,1), P(3,0), P(2,0)),
          BuildTurn(1, P(2,1), P(3,0), P(2,1)),
          BuildTurn(1, P(2,1), P(3,0), P(3,1)),

          BuildTurn(1, P(2,1), P(3,1), P(2,0)),
          BuildTurn(1, P(2,1), P(3,1), P(2,1)),
          BuildTurn(1, P(2,1), P(3,1), P(3,0)),
        )
  }


}
